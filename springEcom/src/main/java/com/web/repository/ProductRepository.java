package com.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /* ==========================================================================
       FIX: Changed p.category to p.category.categoryName (or p.category.name).
       We must traverse into the Category entity string field to execute a string match.
       ========================================================================== */
    @Query("""
           SELECT p FROM Product p
           WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(p.category.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    List<Product> searchProducts(@Param("keyword") String keyword);
}