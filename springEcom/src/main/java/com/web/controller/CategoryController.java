package com.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.web.entity.Category;
import com.web.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    // Get all categories
    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {

        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }


    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(
            @PathVariable Long id) {

        Category category =
                categoryService.getCategoryById(id);

        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(category);
    }


    // Add category
    @PostMapping
    public ResponseEntity<Category> addCategory(
            @RequestBody Category category) {

        Category savedCategory =
                categoryService.addCategory(category);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCategory);
    }


    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody Category category) {

        Category updatedCategory =
                categoryService.updateCategory(id, category);

        if (updatedCategory == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedCategory);
    }


    // Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id) {

        Category category =
                categoryService.getCategoryById(id);

        if (category == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Category not found");
        }

        categoryService.deleteCategory(id);

        return ResponseEntity.ok(
                "Category deleted successfully"
        );
    }
}