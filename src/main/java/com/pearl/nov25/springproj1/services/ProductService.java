package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.ProductInput;
import com.pearl.nov25.springproj1.dtos.response.ProductResponse;
import com.pearl.nov25.springproj1.models.Brand;
import com.pearl.nov25.springproj1.models.Product;
import com.pearl.nov25.springproj1.repositories.BrandRepository;
import com.pearl.nov25.springproj1.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    //save
    @Transactional
    public Product saveProduct(ProductInput productInput) {
        Brand brand = brandRepository.findById(productInput.brandId())
                .orElseThrow(()-> new RuntimeException("Brand not found with id: "+ productInput.brandId()));
        Product product = new Product();
        product.setName(productInput.name());
        product.setPhoto(productInput.photo());
        product.setBarcode(productInput.barCode());
        product.setActiveStatus(productInput.activeStatus());
        product.setBrand(brand);
        return productRepository.save(product);
    }
    //get all list
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }
    //find by id
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id)
                );
    }
    //update by id
    @Transactional
    public Product updateProduct(Long id, ProductInput productInput) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        Brand brand = brandRepository.findById(productInput.brandId())
                .orElseThrow(() -> new RuntimeException("Brand not found with id " + productInput.brandId()));

        existingProduct.setName(productInput.name());
        existingProduct.setBarcode(productInput.barCode());
        existingProduct.setPhoto(productInput.photo());
        existingProduct.setActiveStatus(productInput.activeStatus());
        existingProduct.setBrand(brand);

        return productRepository.save(existingProduct);
    }
    //delete by id
    @Transactional
    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: "+id));
        productRepository.delete(product);
    }

    //product list by Brand
    public List<ProductResponse> findByNaming(Long id) {
        List<Product> products = productRepository.findByProductWithBrandId(id);

        return products.stream().map(p -> {
            ProductResponse res = new ProductResponse();
            res.setId(p.getId());
            res.setName(p.getName());
            res.setBarcode(p.getBarcode());
            return res;
        }).toList();
    }
    public List<ProductResponse> findByProduct(String brandName) {
        List<Product> products = productRepository.findByBrandName(brandName);
        List<ProductResponse> productResponses = products.stream().map(p->{
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(p.getId());
            productResponse.setName(p.getName());
            productResponse.setBarcode(p.getBarcode());
            return productResponse;
        }).toList();
        return productResponses;
    }
}
