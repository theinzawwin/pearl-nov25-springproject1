package com.pearl.nov25.springproj1.controllers;

import com.pearl.nov25.springproj1.dtos.BookInput;
import com.pearl.nov25.springproj1.models.Book;
import com.pearl.nov25.springproj1.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @PostMapping("/save")
    public ResponseEntity<Boolean> save(@RequestBody BookInput bookInput){
        return ResponseEntity.ok(bookService.saveBook(bookInput));
    }
    @GetMapping("/list")
    public ResponseEntity<List<Book>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updateBook(@PathVariable Long id, @RequestBody BookInput bookInput){
        return ResponseEntity.ok(bookService.updateBook(id,bookInput));
    }
    @GetMapping("/{bookId}")
    public ResponseEntity<Optional<Book>> findById(@PathVariable(value = "bookId") Long id){
        return ResponseEntity.ok(bookService.findById(id));
    }
}
