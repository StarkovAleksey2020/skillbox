package com.example.MyBookShopApp.entity.book.review;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonIgnore
    private BookEntity bookEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity userEntity;
}
