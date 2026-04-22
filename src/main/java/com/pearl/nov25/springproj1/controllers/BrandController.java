package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dtos.BrandInput;
import com.pearl.nov25.springproj1.dtos.UpdateBrandStatus;
import com.pearl.nov25.springproj1.dtos.response.BrandResponse;
import com.pearl.nov25.springproj1.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/brands")
@RestController
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/save")
    public ResponseEntity<Boolean> saveBook(@RequestBody BrandInput brandInput){

        boolean result = brandService.saveBrand(brandInput);

        return ResponseEntity.ok(result);

    }
    @GetMapping("/search")
    public ResponseEntity<List<BrandResponse>> getBrandListByName(@RequestParam("brandName")String brandName){
        return ResponseEntity.ok(brandService.findBrandsByNameContaining(brandName));
    }


    @GetMapping("/active-list")
    public ResponseEntity<List<BrandResponse>> getActiveBrandList(){
        return ResponseEntity.ok(brandService.getActiveBrandList());
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<Boolean> updateBrandStatus(@PathVariable(value = "id")Long id,@RequestBody UpdateBrandStatus brandStatus){

        return ResponseEntity.ok(brandService.updateBrandStatus(id,brandStatus));
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<List<BrandResponse>> getBrandByName(@RequestParam("name")String brandName){
        return ResponseEntity.ok(brandService.findByNaming(brandName));
    }
}
