package com.example.MyBookShopApp.entity;

import com.example.MyBookShopApp.entity.genre.GenreEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;

//@Data
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
    private Set<AuthorEntity> authorSet;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getPubDate() {
        return pubDate;
    }

    public void setPubDate(OffsetDateTime pubDate) {
        this.pubDate = pubDate;
    }

    public int getIsBestseller() {
        return isBestseller;
    }

    public void setIsBestseller(int isBestseller) {
        this.isBestseller = isBestseller;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Set<AuthorEntity> getAuthorSet() {
        return authorSet;
    }

    public void setAuthorSet(Set<AuthorEntity> authorSet) {
        this.authorSet = authorSet;
    }

    public Set<GenreEntity> getGenreEntitySet() {
        return genreEntitySet;
    }

    public void setGenreEntitySet(Set<GenreEntity> genreEntitySet) {
        this.genreEntitySet = genreEntitySet;
    }

    public Set<UserEntity> getUserEntityBook2UserSet() {
        return userEntityBook2UserSet;
    }

    public void setUserEntityBook2UserSet(Set<UserEntity> userEntityBook2UserSet) {
        this.userEntityBook2UserSet = userEntityBook2UserSet;
    }

    public Set<UserEntity> getUserEntityFileDownloadSet() {
        return userEntityFileDownloadSet;
    }

    public void setUserEntityFileDownloadSet(Set<UserEntity> userEntityFileDownloadSet) {
        this.userEntityFileDownloadSet = userEntityFileDownloadSet;
    }

    public Set<UserEntity> getUserEntityBalanceTransactionSet() {
        return userEntityBalanceTransactionSet;
    }

    public void setUserEntityBalanceTransactionSet(Set<UserEntity> userEntityBalanceTransactionSet) {
        this.userEntityBalanceTransactionSet = userEntityBalanceTransactionSet;
    }

    public Set<UserEntity> getUserEntityBookReviewSet() {
        return userEntityBookReviewSet;
    }

    public void setUserEntityBookReviewSet(Set<UserEntity> userEntityBookReviewSet) {
        this.userEntityBookReviewSet = userEntityBookReviewSet;
    }
}
