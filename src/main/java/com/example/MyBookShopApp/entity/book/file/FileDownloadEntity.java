package com.example.MyBookShopApp.entity.book.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "file_download")
@ApiModel(description = "the number of downloads of the book by the user")
public class FileDownloadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "User id", position = 2)
    private Long userId;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "Book id", position = 3)
    private Long bookId;

    @Column(name = "count", columnDefinition = "INT NOT NULL DEFAULT 1")
    @ApiModelProperty(value = "Number of downloads", position = 4)
    private int count;

}
