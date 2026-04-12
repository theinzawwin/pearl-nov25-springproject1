package com.pearl.nov25.springproj1.repositories;

import com.pearl.nov25.springproj1.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //native query
    @Query(value= "select * from products where brand_id=:brand_id", nativeQuery = true)
    List<Product> findByProductWithBrandId(@Param("brand_id")Long id);

    //JPQL
    @Query("SELECT p from Product p where p.brand.name = :brandName")
    List<Product> findByBrandName(@Param("brandName")String brandName);
}
