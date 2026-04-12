package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.ProductInput;
import com.pearl.nov25.springproj1.dtos.response.ProductResponse;
import com.pearl.nov25.springproj1.models.Product;
import com.pearl.nov25.springproj1.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public boolean saveProduct(ProductInput productInput){
        Product product = new Product();
        product.setName(productInput.name());
        product.setStatus(productInput.activeStatus());
        product.setCode(productInput.barcode());
        product.setDescription(productInput.description());
        Product addedProduct = productRepository.save(product);
        return true;
    }

    public List<ProductResponse> findProductsByNameContaining(String name){
        List<Product> products = productRepository.findByNameContaining(name);
        List<ProductResponse> productResponses = products.stream().map(p->{
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(p.getId());
            productResponse.setName(p.getName());
            productResponse.setBrandId(p.getBrand().getId());
            return productResponse;
        }).toList();
        return productResponses;
    }

}
