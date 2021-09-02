package com.example.MyBookShopApp.entity.book.links;

import com.example.MyBookShopApp.entity.BookEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book2rate")
@ApiModel(description = "linking rate to books")
public class Book2RateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(name = "rate", columnDefinition = "INT NOT NULL  DEFAULT 0")
    @ApiModelProperty(value = "Book rate", position = 2)
    private Integer rate;

//    @Column(name = "book_id", columnDefinition = "INT NOT NULL  DEFAULT 0")
//    @ApiModelProperty(value = "Book id", position = 3)
//    private Long bookId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @ApiModelProperty(value = "Book id")
    private BookEntity bookEntity;

}
