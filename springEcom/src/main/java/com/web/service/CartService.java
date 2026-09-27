package com.web.service;

import com.web.entity.CartItem;
import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(Long userId);
    void addToCart(Long userId, Long productId); 
    void updateQuantity(Long userId, Long productId, int delta); 
    void removeFromCart(Long userId, Long productId); 
}