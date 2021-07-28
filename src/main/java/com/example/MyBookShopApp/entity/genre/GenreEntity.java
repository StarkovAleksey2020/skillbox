package com.example.MyBookShopApp.entity.genre;

import com.example.MyBookShopApp.entity.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genre")
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT")
    @JoinColumn(name = "parent_id")
    private int parentId;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @JoinColumn(name = "slug")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @JoinColumn(name = "name")
    private String name;

    @ManyToMany(mappedBy = "genreEntitySet")
    private Set<BookEntity> bookEntitySet;

}
