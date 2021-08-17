package com.example.MyBookShopApp.entity.tag;

import com.example.MyBookShopApp.entity.BookEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tag")
@ApiModel(description = "books tag table")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(columnDefinition = "TEXT NOT NULL")
    @ApiModelProperty(value = "Tag name", position = 2)
    private String name;

    @ManyToMany(mappedBy = "tagEntities")
    private Set<BookEntity> bookEntities;

}
