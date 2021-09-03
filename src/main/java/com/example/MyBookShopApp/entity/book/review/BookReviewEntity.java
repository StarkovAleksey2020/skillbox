package com.example.MyBookShopApp.entity.book.review;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Table(name = "book_review")
@ApiModel(description = "book reviews")
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(name = "time", columnDefinition = "TIMESTAMP NOT NULL")
    @ApiModelProperty(value = "Time when the review was left")
    private LocalDateTime time;

    @Column(name = "text", columnDefinition = "TEXT NOT NULL")
    @ApiModelProperty(value = "Review text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity bookEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BookEntity getBookEntity() {
        return bookEntity;
    }

    public void setBookEntity(BookEntity bookEntity) {
        this.bookEntity = bookEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
