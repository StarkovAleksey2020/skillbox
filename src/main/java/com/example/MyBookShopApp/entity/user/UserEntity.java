package com.example.MyBookShopApp.entity.user;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.entity.book.review.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "hash")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @JoinColumn(name = "reg_time")
    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime regTime;

    @JoinColumn(name = "balance")
    @Column(columnDefinition = "INT NOT NULL")
    private int balance;

    @JoinColumn(name = "name")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @ManyToMany(mappedBy = "userEntityBook2UserSet")
    private Set<BookEntity> bookEntityBook2UserSet;

    @ManyToMany(mappedBy = "userEntityFileDownloadSet")
    private Set<BookEntity> bookEntityFileDownloadSet;

    @ManyToMany(mappedBy = "userEntityBalanceTransactionSet")
    private Set<BookEntity> bookEntityBalanceTransactionSet;

    @ManyToMany(mappedBy = "userEntityBookReviewSet")
    private Set<BookEntity> bookEntityBookReviewSet;

    @OneToOne(mappedBy = "user")
    private UserContactEntity userContact;

    @OneToOne(mappedBy = "user")
    private BookReviewLikeEntity bookReviewLike;

    @OneToOne(mappedBy = "user")
    private MessageEntity message;

}
