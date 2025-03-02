package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.order.OrderDto;
import com.base.auth.dto.product.ProductDto;
import com.base.auth.form.order.UpdateOrderForm;
import com.base.auth.form.product.CreateProductForm;
import com.base.auth.form.product.UpdateProductForm;
import com.base.auth.mapper.OrderMapper;
import com.base.auth.mapper.ProductMapper;
import com.base.auth.model.*;
import com.base.auth.model.criteria.OrderCriteria;
import com.base.auth.model.criteria.ProductCriteria;
import com.base.auth.repository.*;
import com.base.auth.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrderController extends ABasicController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORD_C')")
    public ApiResponse<String> create() {
        ApiResponse<String> response = new ApiResponse<>();

        long id = getCurrentUser();

        // check customer exists
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer == null) {
            response.setResult(false);
            response.setCode(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
            response.setMessage("Customer not found");
            return response;
        }

        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElse(null);
        if (cart == null || cart.getCartItems().isEmpty()) {
            response.setResult(false);
            response.setCode(ErrorCode.CART_ERROR_NOT_FOUND);
            response.setMessage("Cart is empty");
            return response;
        }

        Order order = new Order();
        order.setCode(StringUtils.generateUniqueCode(6, orderRepository));
        order.setCustomer(customer);

        List<OrderItem> orderItems = new ArrayList<>();
        Double totalSellOff = 0.0;
        Double totalMoney = 0.0;

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.calculateSinglePrice();
            orderItem.calculateSellOff();

            orderItems.add(orderItem);

            totalSellOff += orderItem.getSellOff();
            totalMoney += orderItem.getSinglePrice();
        }

        order.setOrderItems(orderItems);
        order.setTotalSellOff(totalSellOff);
        order.setTotalMoney(totalMoney);

        // save order
        orderRepository.save(order);

        // save all order items
        orderItemRepository.saveAll(orderItems);

        // remove all cart items
        cartItemRepository.deleteAll(cart.getCartItems());

        response.setMessage("Order created successfully with code: " + order.getCode());
        return response;
    }

    @PutMapping(value = "/change-state", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORD_U')")
    public ApiResponse<String> changeState(@Valid @RequestBody UpdateOrderForm updateOrderForm, BindingResult bindingResult) {
        ApiResponse<String> response = new ApiResponse<>();

        long id = getCurrentUser();

        Account account = accountRepository.findById(id).orElse(null);
        if (account == null || account.getKind() != UserBaseConstant.USER_KIND_ADMIN) {
            response.setResult(false);
            response.setCode(ErrorCode.ACCOUNT_ERROR_NOT_ALLOW_UPDATE_ORDER);
            response.setMessage("User not allowed");
            return response;
        }

        Order order = orderRepository.findById(updateOrderForm.getOrderId()).orElse(null);
        if (order == null) {
            response.setResult(false);
            response.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            response.setMessage("Order not found");
            return response;
        }

        if (updateOrderForm.getState() - order.getState() != 1) {
            response.setResult(false);
            response.setCode(ErrorCode.ORDER_ERROR_INVALID);
            response.setMessage("Order state invalid");
            return response;
        }
        order.setStatus(updateOrderForm.getState());
        orderRepository.save(order);

        response.setMessage("Update order state success");
        return response;
    }

    @PutMapping(value = "/cancel/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORD_CL')")
    public ApiResponse<String> cancelOrder(@PathVariable Long id) {
        ApiResponse<String> response = new ApiResponse<>();

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            response.setResult(false);
            response.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            response.setMessage("Order not found");
            return response;
        }

        order.setStatus(UserBaseConstant.ORDER_STATE_CANCEL);
        orderRepository.save(order);

        response.setMessage("Cancel order state success");
        return response;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORD_V')")
    public ApiResponse<OrderDto> get(@PathVariable("id") Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        OrderDto orderDto = orderMapper.fromEntityToOrderDto(order);
        ApiResponse<OrderDto> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(orderDto);
        apiMessageDto.setMessage("Get order success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORD_L_AD')")
    public ApiMessageDto<ResponseListDto<List<OrderDto>>> listForAdmin(OrderCriteria criteria, Pageable pageable) {
        Page<Order> pageData = orderRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListDto<List<OrderDto>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(orderMapper.fromEntityToOrderDtoList(pageData.getContent()));
        responseListDto.setTotalElements(pageData.getTotalElements());
        responseListDto.setTotalPages(pageData.getTotalPages());

        ApiMessageDto<ResponseListDto<List<OrderDto>>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list orders success");
        return apiMessageDto;
    }

    @GetMapping(value = "/client-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORD_L')")
    public ApiMessageDto<ResponseListDto<List<OrderDto>>> listForUser(OrderCriteria criteria, Pageable pageable) {
        ApiMessageDto<ResponseListDto<List<OrderDto>>> apiMessageDto = new ApiMessageDto<>();

        long id = getCurrentUser();

        Page<Order> pageData = orderRepository.findAllByCustomerId(id, criteria.getSpecification(), pageable);

        ResponseListDto<List<OrderDto>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(orderMapper.fromEntityToOrderDtoList(pageData.getContent()));
        responseListDto.setTotalElements(pageData.getTotalElements());
        responseListDto.setTotalPages(pageData.getTotalPages());

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list orders success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ORD_D')")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ORDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        orderItemRepository.deleteAll(order.getOrderItems());

        orderRepository.delete(order);

        apiMessageDto.setMessage("Delete order success");
        return apiMessageDto;
    }

}
