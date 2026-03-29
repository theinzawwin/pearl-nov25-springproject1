package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dtos.StudentInput;
import com.pearl.nov25.springproj1.models.Student;
import com.pearl.nov25.springproj1.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @GetMapping("/hello")
    public String greeting(){

        return "Hello";
    }
    @PostMapping("/save-student")
    public ResponseEntity<Boolean> createStudent(@Valid @RequestBody StudentInput studentInput){
        return ResponseEntity.ok(studentService.save(studentInput));
    }
    @GetMapping("/list")
    public ResponseEntity<List<Student>> getAllStudent(){
        return ResponseEntity.ok(studentService.getAllStudent());
    }
    @PutMapping("/update-student/{id}")
    public ResponseEntity<Boolean> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentInput studentInput){
        return ResponseEntity.ok(studentService.updateStudent(id,studentInput));
    }
    @GetMapping("{id}")
    public ResponseEntity<Optional<Student>> findById(@PathVariable  Long id){
        return ResponseEntity.ok(studentService.findById(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteStudent(@PathVariable Long id){
        return ResponseEntity.ok(studentService.deleteById(id));
    }
}
