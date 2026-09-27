package com.web.repository;

import com.web.entity.CartItem;
import com.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProductId(User user, Long productId);

    void deleteByUserAndProductId(User user, Long productId);
}