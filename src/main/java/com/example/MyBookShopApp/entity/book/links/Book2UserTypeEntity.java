package com.example.MyBookShopApp.entity.book.links;

import com.example.MyBookShopApp.entity.enumiration.Book2UserTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "book2user_type")
@ApiModel(description = "types of book bindings to users")
public class Book2UserTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "Binding type code (KEPT, CART, PAID, ARCHIVED)")
    private String code;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "Binding type name")
    @Enumerated(EnumType.STRING)
    private Book2UserTypeEnum name;
}
