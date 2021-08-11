package com.example.MyBookShopApp.entity.genre;

import com.example.MyBookShopApp.entity.BookEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genre")
@ApiModel(description = "genres table")
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(columnDefinition = "INT")
    @JoinColumn(name = "parent_id")
    @ApiModelProperty(value = "Parent genre identifier or NULL if genre is root")
    private Long parentId;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @JoinColumn(name = "slug")
    @ApiModelProperty(value = "Genre mnemonic code used in links to a page of that genre")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @JoinColumn(name = "name")
    @ApiModelProperty(value = "Genre name")
    private String name;

    @ManyToMany(mappedBy = "genreEntitySet")
    private Set<BookEntity> bookEntitySet;

}
