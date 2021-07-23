package com.example.MyBookShopApp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

//    @JoinColumn(name = "author_name")
    private String authorName;

    @JoinColumn(name = "title")
    private String title;

    @JoinColumn(name = "price_old")
    private String priceOld;

    @JoinColumn(name = "price")
    private String price;

    public Book(long id, long author, String authorName, String title, String priceOld, String price) {
    }

    public Book() {
    }
}
