package com.example.MyBookShopApp.entity.other;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "faq")
@ApiModel(description = "frequently asked questions and answers to them")
public class FaqEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(columnDefinition = "INT NOT NULL  DEFAULT 0")
    @ApiModelProperty(value = "The serial number of the question in the list of questions on the 'Help' page", position = 2)
    private int sortIndex;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "Question", position = 3)
    private String question;

    @Column(columnDefinition = "TEXT NOT NULL")
    @ApiModelProperty(value = "Answer", position = 4)
    private String answer;
}
