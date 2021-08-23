package com.example.MyBookShopApp.entity.book.review;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "book_review")
@ApiModel(description = "book reviews")
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "Book id")
    private Long bookId;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "User id")
    private Long userId;

    @Column(name = "time", columnDefinition = "TIMESTAMP NOT NULL")
    @ApiModelProperty(value = "Time when the review was left")
    private LocalDateTime time;

    @Column(name = "text", columnDefinition = "TEXT NOT NULL")
    @ApiModelProperty(value = "Review text")
    private String text;

}
