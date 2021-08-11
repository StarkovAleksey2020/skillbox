package com.example.MyBookShopApp.entity.book.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_file_type")
@ApiModel(description = "book file types")
public class BookFileTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "File type name (PDF, EPUB, FB2")
    private String name;

    @Column(columnDefinition = "TEXT")
    @ApiModelProperty(value = "File type description")
    private String description;
}
