package com.pearl.nov25.springproj1.dtos;

public record BookInput(Long id,String name,String isbn, Integer quantity,String author, String publishDate) {
}
