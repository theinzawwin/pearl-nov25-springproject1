package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dtos.BrandInput;
import com.pearl.nov25.springproj1.models.Brand;
import com.pearl.nov25.springproj1.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;


    @PostMapping("/save")
    public ResponseEntity<Boolean> save(@RequestBody BrandInput brandInput) {
        return ResponseEntity.ok(brandService.saveBrand(brandInput));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Brand>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updateBrand(@PathVariable Long id, @RequestBody BrandInput brandInput) {
        return ResponseEntity.ok(brandService.updateBrand(id, brandInput));
    }


    @GetMapping("/{brandId}")
    public ResponseEntity<Optional<Brand>> findById(@PathVariable(value = "brandId") Long id) {
        return ResponseEntity.ok(brandService.findById(id));
    }
}