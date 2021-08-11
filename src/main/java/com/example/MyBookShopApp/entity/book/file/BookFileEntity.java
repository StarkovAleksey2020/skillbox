package com.example.MyBookShopApp.entity.book.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_file")
@ApiModel(description = "Books files")
public class BookFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private int id;

    @JoinColumn(name = "hash")
    @ApiModelProperty(value = "A random hash used to identify the file when downloading", position = 2)
    private String hash;

    @JoinColumn(name = "type_id")
    @ApiModelProperty(value = "File type", position = 3)
    private int typeId;

    @JoinColumn(name = "path")
    @ApiModelProperty(value = "Path to file", position = 4)
    private String path;

}
