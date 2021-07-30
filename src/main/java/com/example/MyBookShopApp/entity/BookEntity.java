package com.example.MyBookShopApp.entity;

import com.example.MyBookShopApp.entity.genre.GenreEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Long id;

    @JoinColumn(name = "pub_date")
    private OffsetDateTime pubDate;

    @JoinColumn(name = "is_bestseller")
    private int isBestseller;

    @JoinColumn(name = "slug")
    private String slug;

    @JoinColumn(name = "title")
    private String title;

    @JoinColumn(name = "image")
    private String image;

    @JoinColumn(name = "description")
    @Column(columnDefinition = "TEXT")
    private String description;

    @JoinColumn(name = "price")
    private int price;

    @JoinColumn(name = "discount")
    private int discount;

    @ManyToMany
    @JoinTable(name = "book2author", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authorSet;

    @ManyToMany
    @JoinTable(name = "book2genre", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<GenreEntity> genreEntitySet;

    @ManyToMany
    @JoinTable(name = "book2user", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> userEntityBook2UserSet;

    @ManyToMany
    @JoinTable(name = "file_download", joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private Set<UserEntity> userEntityFileDownloadSet;

    @ManyToMany
    @JoinTable(name = "balance_transaction", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> userEntityBalanceTransactionSet;

    @ManyToMany
    @JoinTable(name = "book_review", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> userEntityBookReviewSet;

}
