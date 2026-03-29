package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.StudentInput;
import com.pearl.nov25.springproj1.models.Student;
import com.pearl.nov25.springproj1.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;


    public boolean save(StudentInput stdInput){
        Student std = new Student();
        std.setName(stdInput.name());
        std.setRollNo(stdInput.rollNo());
        std.setGender(stdInput.gender());
        std.setFatherName(stdInput.fatherName());
        std.setAddress(stdInput.address());
        std = studentRepository.save(std);
        return true;
    }

    public List<Student> getAllStudent(){
        return studentRepository.findAll();
    }

    public boolean updateStudent(Long id, StudentInput stdInput){
        Student std = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
        std.setName(stdInput.name());
        std.setRollNo(stdInput.rollNo());
        std.setFatherName(stdInput.fatherName());
        std.setGender(stdInput.gender());
        std.setAddress(stdInput.address());
        studentRepository.save(std);
        return true;
    }

    public Optional<Student> findById(Long id){
        return studentRepository.findById(id);
    }

    public boolean deleteById(Long id){
        studentRepository.deleteById(id);
        return true;

    }
}
