package com.example.MyBookShopApp.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @JoinColumn(name = "id")
    private Long id;

    @JoinColumn(name = "author")
    private String author;
}
