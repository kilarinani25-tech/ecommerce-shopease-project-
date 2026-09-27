package com.web.service; // Ensure this matches your directory structure

import com.web.entity.CartItem;
import com.web.entity.Product;
import com.web.entity.User;
import com.web.repository.CartRepository;
import com.web.repository.ProductRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    @Override
    public List<CartItem> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user);
    }

   
    @Override
    public void addToCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartRepository.findByUserAndProductId(user, productId)
                .orElse(new CartItem());
        
        if (item.getId() == null) {
            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(1);
        } else {
            item.setQuantity(item.getQuantity() + 1);
        }
        cartRepository.save(item);
    }

    // CHANGED: productId from Integer to Long
    @Override
    public void updateQuantity(Long userId, Long productId, int delta) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CartItem item = cartRepository.findByUserAndProductId(user, productId)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));
        
        int newQuantity = item.getQuantity() + delta;
        if (newQuantity <= 0) {
            cartRepository.delete(item);
        } else {
            item.setQuantity(newQuantity);
            cartRepository.save(item);
        }
    }

    // CHANGED: productId from Integer to Long
    @Override
    public void removeFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartRepository.deleteByUserAndProductId(user, productId);
    }
}