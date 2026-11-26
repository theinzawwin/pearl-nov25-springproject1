package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.BrandInput;
import com.pearl.nov25.springproj1.dtos.UpdateBrandStatus;
import com.pearl.nov25.springproj1.dtos.response.BrandResponse;
import com.pearl.nov25.springproj1.models.Brand;
import com.pearl.nov25.springproj1.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @CacheEvict(value = "brands", allEntries = true)
    public boolean saveBrand(BrandInput brandInput){
        Brand brand = new Brand();
        brand.setName(brandInput.name());
        brand.setStatus(brandInput.status());
        Brand addedBrand= brandRepository.save(brand);
        return true;
    }

    @Cacheable(value = "brands", key = "#name")
    public List<BrandResponse> findBrandsByNameContaining(String name){
        List<Brand> brands = brandRepository.findByNameContaining(name);
        List<BrandResponse> brandResponses = brands.stream().map(b->{
            BrandResponse brandResponse = new BrandResponse();
            brandResponse.setId(b.getId());
            brandResponse.setName(b.getName());
            brandResponse.setStatus(b.isStatus());
            return brandResponse;
        }).toList();
        return brandResponses;
    }

    @Cacheable(value = "brands", key = "'active-brands'")
    public List<BrandResponse> getActiveBrandList(){
        List<Brand> brands = brandRepository.findByStatusTrue();
        List<BrandResponse> brandResponses = brands.stream().map(b->{
            BrandResponse brandResponse = new BrandResponse();
            brandResponse.setId(b.getId());
            brandResponse.setName(b.getName());
            brandResponse.setStatus(b.isStatus());
            return brandResponse;
        }).toList();
        return brandResponses;
    }


    @CacheEvict(value = "brands", allEntries = true)
    public boolean updateBrandStatus(Long id, UpdateBrandStatus brandStatus){
        int updatedCount = brandRepository.updateStatus(id,brandStatus.status());
        return updatedCount > 0;
    }

    @Cacheable(value = "brands", key = "'exact-' + #name")
    public List<BrandResponse> findByNaming(String name){
       // List<Brand> brands = brandRepository.findByNaming(name);

        // For native query
        List<Brand> brands = brandRepository.findByNameNativeQuery(name);
        List<BrandResponse> brandResponses = brands.stream().map(b->{
            BrandResponse brandResponse = new BrandResponse();
            brandResponse.setId(b.getId());
            brandResponse.setName(b.getName());
            brandResponse.setStatus(b.isStatus());
            return brandResponse;
        }).toList();
        return brandResponses;
    }
}
