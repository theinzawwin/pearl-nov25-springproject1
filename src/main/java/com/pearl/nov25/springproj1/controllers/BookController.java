package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dtos.BookInput;
import com.pearl.nov25.springproj1.models.Book;
import com.pearl.nov25.springproj1.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Boolean> save(@RequestBody BookInput bookInput){
        return ResponseEntity.ok(bookService.saveBook(bookInput));
    }
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<Book>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Boolean> updateBook(@PathVariable Long id, @RequestBody BookInput bookInput){
        return ResponseEntity.ok(bookService.updateBook(id,bookInput));
    }
    @GetMapping("/{bookId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Optional<Book>> findById(@PathVariable(value = "bookId") Long id){
        return ResponseEntity.ok(bookService.findById(id));
    }
}
