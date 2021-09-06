package com.example.MyBookShopApp.entity.book.review;

import com.example.MyBookShopApp.entity.user.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_review_like")
@ApiModel(description = "likes and dislikes of reviews")
public class BookReviewLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @JoinColumn(name = "review_id")
    @Column(columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "Review id")
    private Long reviewId;

    @Column(name = "time", columnDefinition = "TIMESTAMP NOT NULL")
    @ApiModelProperty(value = "The date and time at which the like or dislike was set")
    private LocalDateTime time;

    @Column(name = "value", columnDefinition = "SMALLINT NOT NULL")
    @ApiModelProperty(value = "Like (1) or dislike (-1)")
    private short value;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
