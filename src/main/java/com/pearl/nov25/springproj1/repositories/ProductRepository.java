package com.pearl.nov25.springproj1.repositories;

import com.pearl.nov25.springproj1.dtos.ProductDto;
import com.pearl.nov25.springproj1.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
            SELECT new com.pearl.nov25.springproj1.dtos.ProductDto(
            p.id, p.name, p.barcode, p.activeStatus, b.id, b.name)
            FROM Product p JOIN p.brand b
            """)
    Page<ProductDto> findAllProductDTO(Pageable pageable);
}
