package com.pearl.nov25.springproj1.repositories;

import com.pearl.nov25.springproj1.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByNameContaining(String name);
}
