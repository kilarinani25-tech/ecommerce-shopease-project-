package com.web.controller;

import com.web.entity.CartItem;
import com.web.entity.Product;
import com.web.entity.User;
import com.web.repository.CartRepository;
import com.web.repository.ProductRepository;
import com.web.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;


    // Get logged-in user's cart
    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        List<CartItem> cartItems = cartRepository.findByUser(user);

        return ResponseEntity.ok(cartItems);
    }


    // Add product to cart
    @PostMapping("/add")
    @Transactional
    public ResponseEntity<?> addToCart(
            @RequestBody Map<String, Long> payload,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Long productId = payload.get("productId");

        if (productId == null) {
            return ResponseEntity.badRequest()
                    .body("Product ID is required");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));


        CartItem item = cartRepository
                .findByUserAndProductId(user, productId)
                .orElse(null);


        if (item == null) {

            item = new CartItem();

            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(1);

        } else {

            item.setQuantity(
                    item.getQuantity() + 1
            );
        }


        cartRepository.save(item);

        return ResponseEntity.ok("Product added to cart");
    }


    // Increase / decrease quantity
    @PatchMapping("/update/{productId}")
    @Transactional
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long productId,
            @RequestBody Map<String, Integer> payload,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Integer delta = payload.get("delta");

        if (delta == null) {
            return ResponseEntity.badRequest()
                    .body("Delta is required");
        }


        CartItem item = cartRepository
                .findByUserAndProductId(user, productId)
                .orElseThrow(() ->
                        new RuntimeException("Item not found in cart"));


        int newQuantity =
                item.getQuantity() + delta;


        if (newQuantity <= 0) {

            cartRepository.delete(item);

        } else {

            item.setQuantity(newQuantity);

            cartRepository.save(item);
        }


        return ResponseEntity.ok("Cart updated");
    }


    // Remove product from cart
    @DeleteMapping("/remove/{productId}")
    @Transactional
    public ResponseEntity<?> removeFromCart(
            @PathVariable Long productId,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        cartRepository.deleteByUserAndProductId(
                user,
                productId
        );

        return ResponseEntity.ok("Product removed from cart");
    }


    // Get currently authenticated user
    private User getAuthenticatedUser(
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        String email = authentication.getName();

        return userService.findUserByEmail(email);
    }
}