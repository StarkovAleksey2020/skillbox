package com.example.MyBookShopApp.entity.book.file;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.enumiration.BookFileTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "book_file")
@ApiModel(description = "Books files")
public class BookFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(name = "hash")
    @ApiModelProperty(value = "A random hash used to identify the file when downloading", position = 2)
    private String hash;

    @Column(name = "book_file_type_id")
    @ApiModelProperty(value = "File type", position = 3)
    private Integer typeId;

    @Column(name = "path")
    @ApiModelProperty(value = "Path to file", position = 4)
    private String path;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @JsonIgnore
    private BookEntity book;

    public String getBookFileExtensionString() {
        return BookFileTypeEnum.getExtensionStringByTypeId(typeId);
    }
}
