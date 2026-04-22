package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.ProductInput;
import com.pearl.nov25.springproj1.dtos.UpdateBrandStatus;
import com.pearl.nov25.springproj1.dtos.UpdateProductStatus;
import com.pearl.nov25.springproj1.dtos.response.ProductResponse;
import com.pearl.nov25.springproj1.models.Brand;
import com.pearl.nov25.springproj1.models.Product;
import com.pearl.nov25.springproj1.repositories.BrandRepository;
import com.pearl.nov25.springproj1.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandRepository brandRepository;
    public boolean saveProduct(ProductInput productInput){
        Product product = new Product();
        product.setName(productInput.name());
        product.setStatus(productInput.activeStatus());
        product.setCode(productInput.barcode());
        product.setDescription(productInput.description());
//        Brand brand= brandRepository.findById(productInput.brandId()).orElseThrow();
        product.setBrand(brandRepository.findById(productInput.brandId()).orElseThrow());
        Product addedProduct = productRepository.save(product);
        return true;
    }

    public List<ProductResponse> findProductsByNameContaining(String name){
        List<Product> products = productRepository.findByNameContaining(name);
        List<ProductResponse> productResponses = products.stream().map(p->{
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(p.getId());
            productResponse.setName(p.getName());
            productResponse.setBrand(p.getBrand());
            return productResponse;
        }).toList();
        return productResponses;
    }


    public List<ProductResponse> getActiveProductList(){
       List<Product> products =productRepository.findByStatusTrue();
       List<ProductResponse> productResponses=products.stream().map(p->{
           ProductResponse productResponse = new ProductResponse();
           productResponse.setId(p.getId());
           productResponse.setName(p.getName());
           productResponse.setCode(p.getCode());
           productResponse.setDescription(p.getDescription());

           productResponse.setBrand(p.getBrand());
           productResponse.setStatus(p.isStatus());
//           productResponse.setBrandId(p.getBrand().getId());
           return productResponse;
       }).toList();
       return productResponses;
    }
    public boolean updateProductStatus(Long id, UpdateProductStatus brandStatus){
        int updatedCount = productRepository.updateStatus(id,brandStatus.status());
        return updatedCount > 0;
    }

}
