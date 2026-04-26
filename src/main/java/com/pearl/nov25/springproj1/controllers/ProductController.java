package com.pearl.nov25.springproj1.controllers;


import com.pearl.nov25.springproj1.dtos.ProductInput;
import com.pearl.nov25.springproj1.models.Product;
import com.pearl.nov25.springproj1.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/save")
    public ResponseEntity<Boolean> createProduct(@Valid @RequestBody ProductInput productInput) {
        return ResponseEntity.ok(productService.save(productInput));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductInput productInput) {
        return ResponseEntity.ok(productService.updateProduct(id, productInput));
    }
}