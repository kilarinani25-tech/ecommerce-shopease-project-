package com.web.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web.entity.Category;
import com.web.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    // Add Category
    public Category addCategory(Category category) {
        return repo.save(category);
    }

    // Get All Categories
    public List<Category> getAllCategories() {
        return repo.findAll();
    }

    // Get Category By Id - CHANGED to Long
    public Category getCategoryById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Update Category - CHANGED to Long
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = repo.findById(id).orElse(null);

        if (existingCategory != null) {
            existingCategory.setCategoryName(category.getCategoryName());
            return repo.save(existingCategory);
        }
        return null;
    }

    // Delete Category - CHANGED to Long
    public void deleteCategory(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }
}