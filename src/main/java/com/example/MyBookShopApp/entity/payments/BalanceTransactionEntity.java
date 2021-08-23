package com.example.MyBookShopApp.entity.payments;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "balance_transaction")
@ApiModel(description = "transactions on user accounts")
public class BalanceTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Integer id;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "User id")
    private Long userId;

    @Column(name = "time", columnDefinition = "TIMESTAMP NOT NULL")
    @ApiModelProperty(value = "Date and time of the transaction")
    private LocalDateTime time;

    @Column(name = "value", columnDefinition = "INT NOT NULL  DEFAULT 0")
    @ApiModelProperty(value = "Transaction size (positive - crediting, negative - debiting)")
    private Long value;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    @ApiModelProperty(value = "The book for the purchase of which there was a write-off, or NULL")
    private Long bookId;

    @Column(name = "description", columnDefinition = "TEXT NOT NULL")
    @ApiModelProperty(value = "Description of the transaction: if credited, then where, if debited, then for what")
    private String description;

}
