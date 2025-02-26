package com.base.auth.controller;

import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
import com.base.auth.form.product.ProductCartForm;
import com.base.auth.model.Cart;
import com.base.auth.model.CartItem;
import com.base.auth.model.Customer;
import com.base.auth.model.Product;
import com.base.auth.repository.CartItemRepository;
import com.base.auth.repository.CartRepository;
import com.base.auth.repository.CustomerRepository;
import com.base.auth.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/update")
    public ApiResponse<String> updateCart(@RequestBody List<ProductCartForm> productCartForms) {
        ApiResponse<String> response = new ApiResponse<>();

        // get username from context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            response.setResult(false);
            response.setCode(ErrorCode.ACCOUNT_ERROR_LOGIN);
            response.setMessage("User not authenticated");
            return response;
        }

        String username = authentication.getName();

        // check customer exists
        Customer customer = customerRepository.findCustomerByAccountUsername(username)
                .orElse(null);

        if (customer == null) {
            response.setResult(false);
            response.setCode(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
            response.setMessage("Customer not found");
            return response;
        }

        Map<Long, Product> productMap = productRepository.findAllById(
                productCartForms.stream()
                        .map(ProductCartForm::getProductId)
                        .collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(Product::getId, p -> p));

        // product list empty
        if (productMap.isEmpty()) {
            response.setResult(false);
            response.setCode(ErrorCode.CART_ERROR_INVALID);
            response.setMessage("Product list cannot be empty.");
            return response;
        }

        // check products exists
        List<Long> invalidProductIds = productCartForms.stream()
                .map(ProductCartForm::getProductId)
                .filter(id -> !productMap.containsKey(id))
                .collect(Collectors.toList());

        if (!invalidProductIds.isEmpty()) {
            response.setResult(false);
            response.setCode(ErrorCode.CART_ERROR_NOT_FOUND);
            response.setMessage("Products not found: " + invalidProductIds);
            return response;
        }

        // find cart by customer id
        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElse(null);
        if (cart == null) {
            response.setResult(false);
            response.setCode(ErrorCode.CART_ERROR_NOT_FOUND);
            return response;
        }

        // get cartItems by cartId
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        List<CartItem> itemsToRemove = cartItems.stream()
                .filter(cartItem -> !productMap.containsKey(cartItem.getProduct().getId()))
                .collect(Collectors.toList());

        if (!itemsToRemove.isEmpty()) {
            cartItemRepository.deleteAll(itemsToRemove);
            cartItems.removeAll(itemsToRemove);
        }

        Map<Long, CartItem> cartItemMap = cartItems.stream()
                .collect(Collectors.toMap(ci -> ci.getProduct().getId(), ci -> ci));

        for (ProductCartForm productCartForm : productCartForms) {
            // contains product -> update quantity
            if (cartItemMap.containsKey(productCartForm.getProductId())) {
                CartItem cartItem = cartItemMap.get(productCartForm.getProductId());
                cartItem.setQuantity(productCartForm.getQuantity());
                cartItemRepository.save(cartItem);
            } else {
                // quantity = 1
                Product product = productMap.get(productCartForm.getProductId());
                CartItem newCartItem = new CartItem(cart, product);
                cartItemRepository.save(newCartItem);
            }
        }

        response.setMessage("Cart updated successfully!");
        return response;
    }

}
