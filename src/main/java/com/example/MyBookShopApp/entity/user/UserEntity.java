package com.example.MyBookShopApp.entity.user;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.review.BookReviewEntity;
import com.example.MyBookShopApp.entity.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.entity.book.review.MessageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_entity")
@ApiModel(description = "Bookshop users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @JoinColumn(name = "hash")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "Hash of the user, used for external identification of the user in order to hide his ID")
    private String hash;

    @JoinColumn(name = "reg_time")
    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    @ApiModelProperty(value = "Date and time of registration")
    private LocalDateTime regTime;

    @JoinColumn(name = "balance")
    @Column(columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "Personal account balance, default 0")
    private Integer balance;

    @JoinColumn(name = "name")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "User name")
    private String name;

    @ManyToMany(mappedBy = "userEntityBook2UserSet")
    private Set<BookEntity> bookEntityBook2UserSet;

    @ManyToMany(mappedBy = "userEntityFileDownloadSet")
    private Set<BookEntity> bookEntityFileDownloadSet;

    @ManyToMany(mappedBy = "userEntityBalanceTransactionSet")
    private Set<BookEntity> bookEntityBalanceTransactionSet;

    @OneToOne(mappedBy = "user")
    private BookReviewLikeEntity bookReviewLike;

    @OneToOne(mappedBy = "user")
    private MessageEntity message;

    @OneToMany(mappedBy = "userEntity")
    private List<BookReviewEntity> bookReviewEntities;

    @OneToOne(mappedBy = "userEntity")
    private UserContactEntity userContact;
}
