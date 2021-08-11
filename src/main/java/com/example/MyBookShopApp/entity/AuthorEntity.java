package com.example.MyBookShopApp.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
@ApiModel(description = "author data model")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @JoinColumn(name = "photo")
    @ApiModelProperty(value = "authors photo", position = 2)
    private String photo;

    @JoinColumn(name = "slug")
    @ApiModelProperty("Author's mnemonic identifier")
    private String slug;

    @JoinColumn(name = "name")
    @ApiModelProperty(value = "authors name", example = "John Keith Laumer", position = 3)
    private String name;

    @JoinColumn(name = "description")
    @Column(columnDefinition = "TEXT")
    @ApiModelProperty(value = "authors description", position = 4)
    private String description;

    @EqualsAndHashCode.Exclude @ToString.Exclude
    @ManyToMany(mappedBy = "authorSet")
    private Set<BookEntity> bookEntitySet;
}
