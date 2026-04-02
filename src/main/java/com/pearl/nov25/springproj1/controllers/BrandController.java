package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dtos.BrandInput;
import com.pearl.nov25.springproj1.models.Brand;
import com.pearl.nov25.springproj1.services.BrandService;
import org.apache.el.parser.BooleanNode;
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

    //save
    @PostMapping("/save")
    public ResponseEntity<Boolean> saveBrand(@RequestBody BrandInput brandInput) {
        return ResponseEntity.ok(brandService.saveBrand(brandInput));
    }
    //get all list
    @GetMapping("/list")
    public ResponseEntity<List<Brand>> getAllBrand() {
        return ResponseEntity.ok(brandService.getAllBrand());
    }
    //find by id
    @GetMapping("find/{brandId}")
    public ResponseEntity<Optional<Brand>> findById(@PathVariable(value = "brandId") Long id) {
        return ResponseEntity.ok(brandService.findById(id));
    }
    //update by id
    @PutMapping("/update/{brandId}")
    public ResponseEntity<Boolean> updateBrand(@PathVariable(value = "brandId") Long id, @RequestBody BrandInput brandInput) {
        return ResponseEntity.ok(brandService.updateBrand(id, brandInput));
    }
    @DeleteMapping("/delete/{brandId}")
    public ResponseEntity<Boolean> deleteBrand(@PathVariable(value = "brandId") Long id) {
        return ResponseEntity.ok(brandService.deleteById(id));
    }
}
