package com.example.MyBookShopApp.entity.book.links;

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

@Table(name = "book2tag")
@ApiModel(description = "linking books to tag")
public class Book2TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "Book id")
    private Long bookId;

    @Column(name = "tag_id", columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "Tag id")
    private Long tagId;
}
