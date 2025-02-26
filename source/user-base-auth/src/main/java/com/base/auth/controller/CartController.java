package com.base.auth.controller;

import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
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
    public ApiResponse<String> updateCart(@RequestParam Long customerId, @RequestBody List<Long> productIds) {
        ApiResponse<String> response = new ApiResponse<>();

        // check customer exists
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            response.setResult(false);
            response.setCode(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
            response.setMessage("Customer not found: " + customerId);
            return response;
        }

        List<Product> validProducts = productRepository.findAllById(productIds);
        Set<Long> validProductIds = validProducts.stream().map(Product::getId).collect(Collectors.toSet());

        // product list empty
        if (validProductIds.isEmpty()) {
            response.setResult(false);
            response.setCode(ErrorCode.CART_ERROR_INVALID);
            response.setMessage("Product list cannot be empty.");
            return response;
        }

        // check products exists
        List<Long> invalidProductIds = productIds.stream()
                .filter(id -> !validProductIds.contains(id))
                .collect(Collectors.toList());

        if (!invalidProductIds.isEmpty()) {
            response.setResult(false);
            response.setCode(ErrorCode.CART_ERROR_NOT_FOUND);
            response.setMessage("Products not found: " + invalidProductIds);
            return response;
        }

        // find cart by customer id
        Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);
        if (cart == null) {
            response.setResult(false);
            response.setCode(ErrorCode.CART_ERROR_NOT_FOUND);
            return response;
        }

        // get cartItems by cartId
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        Map<Long, CartItem> cartItemMap = cartItems.stream()
                .collect(Collectors.toMap(ci -> ci.getProduct().getId(), ci -> ci));


        for (Product product : validProducts) {
            // contains product -> quantity + 1
            if (cartItemMap.containsKey(product.getId())) {
                CartItem cartItem = cartItemMap.get(product.getId());
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItemRepository.save(cartItem);
            } else {
                // quantity = 1
                CartItem newCartItem = new CartItem(cart, product);
                cartItemRepository.save(newCartItem);
            }
        }

        // remove product
        List<CartItem> itemsToRemove = cartItems.stream()
                .filter(cartItem -> !validProductIds.contains(cartItem.getProduct().getId()))
                .collect(Collectors.toList());
        cartItemRepository.deleteAll(itemsToRemove);

        response.setMessage("Cart updated successfully!");
        return response;
    }

}
