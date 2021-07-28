package com.example.MyBookShopApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
}
