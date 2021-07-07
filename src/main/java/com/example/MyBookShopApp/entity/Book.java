package com.example.MyBookShopApp.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @JoinColumn(name = "id")
    private Long id;

    @JoinColumn(name = "author")
    private Long author;

    private String authorName;

    @JoinColumn(name = "title")
    private String title;

    @JoinColumn(name = "priceOld")
    private String priceOld;

    @JoinColumn(name = "price")
    private String price;

    public Book(long id, long author, String authorName, String title, String priceOld, String price) {
    }

    public Book() {
    }
}
