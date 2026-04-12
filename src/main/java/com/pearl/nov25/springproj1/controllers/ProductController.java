package com.pearl.nov25.springproj1.controllers;


import com.pearl.nov25.springproj1.dtos.ProductInput;
import com.pearl.nov25.springproj1.dtos.response.ProductResponse;
import com.pearl.nov25.springproj1.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/products")
public class ProductController {



        @Autowired
        private ProductService productService;

        @PostMapping("/save")
        public ResponseEntity<Boolean> saveBook(@RequestBody ProductInput productInput){

            boolean result = productService.saveProduct(productInput);

            return ResponseEntity.ok(result);

        }
        @GetMapping("/search")
        public ResponseEntity<List<ProductResponse>> getProductListByName(@RequestParam("productName")String name){
            return ResponseEntity.ok(productService.findProductsByNameContaining(name));
        }

//
//        @GetMapping("/active-list")
//        public ResponseEntity<List<BrandResponse>> getActiveBrandList(){
//            return ResponseEntity.ok(brandService.getActiveBrandList());
//        }
//
//        @PutMapping("/update-status/{id}")
//        public ResponseEntity<Boolean> updateBrandStatus(@PathVariable(value = "id")Long id,@RequestBody UpdateBrandStatus brandStatus){
//
//            return ResponseEntity.ok(brandService.updateBrandStatus(id,brandStatus));
//        }
//
//        @GetMapping("/find-by-name")
//        public ResponseEntity<List<BrandResponse>> getBrandByName(@RequestParam("name")String brandName){
//            return ResponseEntity.ok(brandService.findByNaming(brandName));
//        }
//    }

}
