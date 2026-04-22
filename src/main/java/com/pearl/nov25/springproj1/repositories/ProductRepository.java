package com.pearl.nov25.springproj1.repositories;

import com.pearl.nov25.springproj1.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByNameContaining(String name);

    List<Product> findByStatusTrue();
    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.status=:status where p.id=:id")
    int updateStatus(@Param("id")Long id,@Param("status")boolean status);


    @Query("select p from Product p where p.name=:name")
    List<Product> findByNaming(@Param("name")String name);
    @Query(value="select * from products where brand_name like :name%",nativeQuery = true)
    List<Product> findByNameNativeQuery(@Param("name") String name);
}
