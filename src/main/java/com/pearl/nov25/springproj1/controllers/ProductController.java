package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dtos.ProductInput;
import com.pearl.nov25.springproj1.dtos.response.ProductResponse;
import com.pearl.nov25.springproj1.models.Product;
import com.pearl.nov25.springproj1.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //save
    @PostMapping("/save")
    public ResponseEntity<Product> save (@RequestBody ProductInput productInput) {
        return ResponseEntity.ok(productService.saveProduct(productInput));
    }
    //get all
    @GetMapping("/list")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAllProduct());
    }
    //get by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }
    //update
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductInput productInput) {
        return ResponseEntity.ok(productService.updateProduct(id, productInput));
    }
    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete (@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("find_product/{id}")
    public ResponseEntity<List<ProductResponse>> findProductByBrand(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findByNaming(id));
    }
    @GetMapping("find_product/name/{brandName}")
    public ResponseEntity<List<ProductResponse>> findByProduct(@PathVariable String brandName) {
        return ResponseEntity.ok(productService.findByProduct(brandName));
    }
}
