package com.pearl.nov25.springproj1.services;

import com.pearl.nov25.springproj1.dtos.BookInput;
import com.pearl.nov25.springproj1.models.Book;
import com.pearl.nov25.springproj1.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    public boolean saveBook(BookInput bookInput){

        Book book =new Book();
        book.setName(bookInput.name());
        book.setIsbn(bookInput.isbn());
        book.setAuthor(bookInput.author());
        book.setQuantity(bookInput.quantity());
        book.setPublishDate(bookInput.publishDate());
        bookRepository.save(book);
        return true;
    }

    public Optional<Book> findById(Long id){
        return bookRepository.findById(id);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public boolean updateBook(Long id, BookInput bookInput){
        Book existingBook = bookRepository.findById(id).orElseThrow(()-> new RuntimeException("No Record for this book Id"+id));
        existingBook.setName(bookInput.name());
        existingBook.setAuthor(bookInput.author());
        existingBook.setIsbn(bookInput.isbn());
        if(bookInput.quantity()>0){
            existingBook.setQuantity(bookInput.quantity());
        }
        existingBook.setPublishDate(bookInput.publishDate());
        bookRepository.save(existingBook);
        return true;
    }
}
