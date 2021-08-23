package com.example.MyBookShopApp.entity.book.file;

import com.example.MyBookShopApp.entity.enumiration.BookFileTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Data
@ToString
@EqualsAndHashCode
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
    @Enumerated(EnumType.STRING)
    private BookFileTypeEnum name;

    @Column(columnDefinition = "TEXT")
    @ApiModelProperty(value = "File type description")
    private String description;
}
