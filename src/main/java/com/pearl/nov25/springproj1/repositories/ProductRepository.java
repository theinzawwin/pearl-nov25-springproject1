package com.pearl.nov25.springproj1.repositories;

import com.pearl.nov25.springproj1.dtos.ProductDto;
import com.pearl.nov25.springproj1.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p from Product p where p.code=:code")
    Optional<Product> findByCode(@Param("code") String code);
    @Query("select p from Product p JOIN FETCH p.brand where p.brand.id=:brandId")
    List<Product> findByBrand(@Param("brandId")Long brandId);

    @Query("""
        SELECT new com.pearl.nov25.springproj1.dtos.ProductDto(
            p.id,
            p.name,
            p.code,
            p.status,
            p.description,
            b.id,
            b.name
        )
        FROM Product p
        JOIN p.brand b
    """)
    Page<ProductDto> findAllProductDTO(Pageable pageable);
    @Modifying
    @Transactional
    @Query("DELETE Product p where p.id=:id")
    int deleteProductById(@Param("id")Long id);
}
