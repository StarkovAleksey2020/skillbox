package com.example.MyBookShopApp.entity.book.links;

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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book2rate")
@ApiModel(description = "linking rate to books")
public class Book2RateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(name = "rate", columnDefinition = "INT NOT NULL  DEFAULT 0")
    @ApiModelProperty(value = "Book rate", position = 2)
    private Integer rate;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnore
    private BookEntity bookEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity userEntity;

}
