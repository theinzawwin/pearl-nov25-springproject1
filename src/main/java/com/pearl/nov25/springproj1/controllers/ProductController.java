package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dtos.ProductDto;
import com.pearl.nov25.springproj1.dtos.ProductInput;
import com.pearl.nov25.springproj1.dtos.response.ProductResponse;
import com.pearl.nov25.springproj1.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("product")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<Boolean> saveProduct(@RequestBody ProductInput productInput){
        return ResponseEntity.ok(productService.saveProduct(productInput));
    }

    @GetMapping("/get-products-by-code")
    public ResponseEntity<Optional<ProductResponse>> getProductByCode(@RequestParam(value = "code")String code){
        return ResponseEntity.ok(productService.findProductByCode(code));
    }
    @GetMapping("/get-products-by-brand")
    public ResponseEntity<List<ProductResponse>> getProductListByBrand(@RequestParam(value="brandId")Long brandId){
        return ResponseEntity.ok(productService.getProductListByBrandId(brandId));
    }

    @GetMapping("/list-page")
    public Page<ProductDto> getAllProducts(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        return productService.getAllProducts(pageable);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(value = "id")Long id){
        return ResponseEntity.ok(productService.deleteProductById(id));
    }
}
