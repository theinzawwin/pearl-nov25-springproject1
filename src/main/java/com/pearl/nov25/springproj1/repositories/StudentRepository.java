package com.pearl.nov25.springproj1.repositories;

import com.pearl.nov25.springproj1.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
}
