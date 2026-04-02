package com.pearl.nov25.springproj1.repositories;

import com.pearl.nov25.springproj1.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository <Brand, Long> {
}
