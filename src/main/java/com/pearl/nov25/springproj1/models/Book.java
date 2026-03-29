package com.pearl.nov25.springproj1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "book_name",length = 50)
    private String name;
    @Column(name = "isbn",length = 20)
    private String isbn;
    @Column(name = "author",length = 50)
    private String author;
    private Integer quantity;
    @Column(name = "publish_date")
    private String publishDate;

}
