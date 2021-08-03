package com.example.MyBookShopApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

//@Data
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<BookEntity> getBookEntitySet() {
        return bookEntitySet;
    }

    public void setBookEntitySet(Set<BookEntity> bookEntitySet) {
        this.bookEntitySet = bookEntitySet;
    }
}
