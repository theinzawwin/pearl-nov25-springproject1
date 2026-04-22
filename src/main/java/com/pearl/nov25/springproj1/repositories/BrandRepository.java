package com.pearl.nov25.springproj1.repositories;

import com.pearl.nov25.springproj1.models.Brand;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {

    List<Brand> findByNameContaining(String name);

    List<Brand> findByStatusTrue();

    Optional<Brand> findById(Long id);
    // Modifying Queries
    @Transactional
    @Modifying
    @Query("UPDATE Brand b SET b.status=:status where b.id=:id")
    int updateStatus(@Param("id")Long id,@Param("status")boolean status);

    @Query("select b from Brand b where b.name=:name")
    List<Brand> findByNaming(@Param("name")String name);
    @Query(value = "select * from brands where brand_name like :name%",nativeQuery = true)
    List<Brand> findByNameNativeQuery(@Param("name") String name);
}
