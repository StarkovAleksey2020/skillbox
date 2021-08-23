package com.example.MyBookShopApp.entity.book.links;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book2author")
@ApiModel(description = "linking authors to books")
public class Book2AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "Book id")
    private Long bookId;

    @Column(name = "author_id", columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "Author id")
    private Long authorId;

    @Column(name = "sort_index", columnDefinition = "INT NOT NULL  DEFAULT 0")
    @ApiModelProperty(value = "Serial number of the author")
    private Integer sortIndex;
}
