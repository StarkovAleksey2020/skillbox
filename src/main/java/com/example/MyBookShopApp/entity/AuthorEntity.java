package com.example.MyBookShopApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Long id;

    @JoinColumn(name = "photo")
    private String photo;

    @JoinColumn(name = "slug")
    private String slug;

    @JoinColumn(name = "name")
    private String name;

    @JoinColumn(name = "description")
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "authorSet")
    private Set<BookEntity> bookEntitySet;


}
