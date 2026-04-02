package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.BrandInput;
import com.pearl.nov25.springproj1.models.Brand;
import com.pearl.nov25.springproj1.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;
    //save
    public boolean saveBrand(BrandInput brandInput) {
        Brand brand = new Brand();

        brand.setName(brandInput.name());
        brand.setPhoto(brandInput.photo());
        brand.setActiveStatus(brandInput.activeStatus());
        brandRepository.save(brand);
        return true;
    }
    //get all list
    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }
    //find by id
    public Optional<Brand> findById (Long id) {
        return brandRepository.findById(id);
    }
    //update by id
    public boolean updateBrand(Long id, BrandInput brandInput) {
        Brand existingBrand = brandRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No Record for this brand " + id));

        existingBrand.setName(brandInput.name());
        existingBrand.setPhoto(brandInput.photo());
        existingBrand.setActiveStatus(brandInput.activeStatus());
        brandRepository.save(existingBrand);
        return true;
    }
    //delete by id
    public boolean deleteById(Long id) {
        brandRepository.deleteById(id);
        return true;
    }

}
