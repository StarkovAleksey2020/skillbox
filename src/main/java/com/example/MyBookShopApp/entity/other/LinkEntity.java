package com.example.MyBookShopApp.entity.other;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "links")
@ApiModel(description = "HATEOAS links")
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(columnDefinition = "VARCHAR(500) NOT NULL")
    private String url;

}
