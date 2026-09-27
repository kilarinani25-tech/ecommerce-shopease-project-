package com.web.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.web.entity.Product;
import com.web.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {

        return ResponseEntity.ok(
                productService.getProducts()
        );
    }


    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id) {

        Product product =
                productService.getProductById(id);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }


    // Get product image
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImageByProductId(
            @PathVariable Long id) {

        Product product =
                productService.getProductById(id);

        if (product == null ||
                product.getImageData() == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .ok()
                .contentType(
                        MediaType.parseMediaType(
                                product.getImageType()
                        )
                )
                .body(product.getImageData());
    }


    // Add product
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> addProduct(

            @RequestPart("product")
            Product product,

            @RequestPart("imageFile")
            MultipartFile imageFile) {

        try {

            Product savedProduct =
                    productService.addProduct(
                            product,
                            imageFile
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedProduct);

        } catch (IOException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image");
        }
    }


    // Update product
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> updateProduct(

            @PathVariable Long id,

            @RequestPart("product")
            Product product,

            @RequestPart(
                    value = "imageFile",
                    required = false
            )
            MultipartFile imageFile) {

        try {

            product.setId(id);

            Product updatedProduct =
                    productService.updateProduct(
                            product,
                            imageFile
                    );

            return ResponseEntity.ok(
                    updatedProduct
            );

        } catch (IOException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update product");
        }
    }


    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id) {

        Product product =
                productService.getProductById(id);

        if (product == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                "Product deleted successfully"
        );
    }


    // Search products
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                productService.searchProducts(keyword)
        );
    }
}