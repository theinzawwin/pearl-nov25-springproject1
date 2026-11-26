package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.ProductDto;
import com.pearl.nov25.springproj1.dtos.ProductInput;
import com.pearl.nov25.springproj1.dtos.response.BrandResponse;
import com.pearl.nov25.springproj1.dtos.response.ProductResponse;
import com.pearl.nov25.springproj1.models.Brand;
import com.pearl.nov25.springproj1.models.Product;
import com.pearl.nov25.springproj1.repositories.BrandRepository;
import com.pearl.nov25.springproj1.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandRepository brandRepository;

    public boolean saveProduct(ProductInput productInput) {
        Brand brand = brandRepository.findById(productInput.getBrandId()).orElseThrow( ()-> new RuntimeException("Invalid Brand"));
        Product product = new Product();
        product.setName(productInput.getName());
        product.setCode(productInput.getCode());
        product.setStatus(productInput.isStatus());
        product.setDescription(productInput.getDescription());
        product.setBrand(brand);

        productRepository.save(product);
        return true;
    }

    public Optional<ProductResponse> findProductByCode(String code){
        Optional<Product> p=productRepository.findByCode(code);
        if(p.isPresent()){
            Product product = p.get();
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setCode(product.getCode());
            productResponse.setDescription(product.getDescription());
            productResponse.setBrandId(product.getBrand().getId());
            productResponse.setBrandName(product.getBrand().getName());
            return Optional.of(productResponse);
        }else{
            return Optional.empty();
        }
    }

    public List<ProductResponse> getProductListByBrandId(Long brandId){
        List<Product> productList = productRepository.findByBrand(brandId);
        List<ProductResponse> productResponseList = productList.stream().map(b->{
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(b.getId());
            productResponse.setName(b.getName());
            productResponse.setStatus(b.isStatus());
            productResponse.setBrandId(b.getBrand().getId());
            productResponse.setBrandName(b.getBrand().getName());
            return productResponse;
        }).toList();
        return productResponseList;
    }

    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAllProductDTO(pageable);
    }

    public boolean deleteProductById(Long id){
        int deletedCount = productRepository.deleteProductById(id);
        return deletedCount>0;
    }

}
