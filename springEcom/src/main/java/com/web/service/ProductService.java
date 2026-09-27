package com.web.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.entity.Product;
import com.web.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository prepo;

    public List<Product> getProducts() {
        return prepo.findAll();
    }

    // CHANGED: Parameter from int to Long
    public Product getProductById(Long id) {
        return prepo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());
        return prepo.save(product);
    }

    public Product updateProduct(Product product, MultipartFile image) throws IOException {

        Product existingProduct = prepo.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setReleaseDate(product.getReleaseDate());
        existingProduct.setProductAvailable(product.isProductAvailable());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setRating(product.getRating());
        existingProduct.setCategory(product.getCategory());

        if (image != null && !image.isEmpty()) {
            existingProduct.setImageName(image.getOriginalFilename());
            existingProduct.setImageType(image.getContentType());
            existingProduct.setImageData(image.getBytes());
        }

        return prepo.save(existingProduct);
    }

   
    public void deleteProduct(Long id) {
        prepo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return prepo.searchProducts(keyword);
    }
}