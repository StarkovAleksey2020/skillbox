package com.example.MyBookShopApp.entity;

import com.example.MyBookShopApp.entity.book.file.BookFileEntity;
import com.example.MyBookShopApp.entity.book.links.Book2RateEntity;
import com.example.MyBookShopApp.entity.book.review.BookReviewEntity;
import com.example.MyBookShopApp.entity.genre.GenreEntity;
import com.example.MyBookShopApp.entity.tag.TagEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "book")
@ApiModel(description = "Entity representing a book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    @ApiModelProperty("Auto generated field (by DB)")
    private Long id;

    @JoinColumn(name = "pub_date")
    @ApiModelProperty("Book publication date")
    private OffsetDateTime pubDate;

    @JoinColumn(name = "is_bestseller")
    @ApiModelProperty("Bestseller tag field")
    private int isBestseller;

    @JoinColumn(name = "slug")
    @ApiModelProperty("Book mnemonic identifier")
    private String slug;

    @JoinColumn(name = "title")
    @ApiModelProperty("Book title field")
    private String title;

    @JoinColumn(name = "image")
    @ApiModelProperty("Cover image")
    private String image;

    @JoinColumn(name = "description")
    @Column(columnDefinition = "TEXT")
    @ApiModelProperty("Book description")
    private String description;

    @JoinColumn(name = "price")
    @ApiModelProperty("Book price")
    private int price;

    @JoinColumn(name = "discount")
    @ApiModelProperty("Discount percentage")
    private Double discount;

    @JsonGetter("authorName")
    public String getAuthorName() {
        return authorSet.get(0).getName();
    }

    @ManyToMany
    @JoinTable(name = "book2author", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @JsonIgnore
    private List<AuthorEntity> authorSet;

    @ManyToMany
    @JoinTable(name = "book2genre", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @JsonIgnore
    private List<GenreEntity> genreEntitySet;

    @ManyToMany
    @JoinTable(name = "book2user", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private List<UserEntity> userEntityBook2UserSet;

    @ManyToMany
    @JoinTable(name = "file_download", joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @JsonIgnore
    private List<UserEntity> userEntityFileDownloadSet;

    @ManyToMany
    @JoinTable(name = "balance_transaction", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private List<UserEntity> userEntityBalanceTransactionSet;

    @ManyToMany
    @JoinTable(name = "book2tag", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnore
    private List<TagEntity> tagEntities;

    @OneToMany(mappedBy = "book")
    private List<BookFileEntity> bookFileList = new ArrayList<>();

    @OneToMany(mappedBy = "bookEntity")
    private List<Book2RateEntity> book2RateEntities;

    @OneToMany(mappedBy = "bookEntity")
    private List<BookReviewEntity> bookReviewEntities;

}
