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

@Table(name = "document")
@ApiModel(description = "the documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(columnDefinition = "INT NOT NULL  DEFAULT 0")
    @ApiModelProperty(value = "Serial number of the document (to be displayed on the page of the list of documents)", position = 2)
    private Integer sortIndex;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "The document mnemonic displayed in the link to the document page", position = 3)
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "Title of the document", position = 4)
    private String title;

    @Column(columnDefinition = "TEXT NOT NULL")
    @ApiModelProperty(value = "HTML document text", position = 5)
    private String text;
}
