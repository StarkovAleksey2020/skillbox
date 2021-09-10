package com.example.MyBookShopApp.entity.cart;

import com.example.MyBookShopApp.entity.user.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "cart")
@ApiModel(description = "Entity representing a cart/postponed")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    @ApiModelProperty("Auto generated field (by DB)")
    private Long id;

    @JoinColumn(name = "value")
    @ApiModelProperty("Cart/postponed json string")
    private String value;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ApiModelProperty(value = "The identifier of the user this cart belongs to")
    private UserEntity userEntity;

    @JoinColumn(name = "edit_date")
    @ApiModelProperty("Cart/postponed string edit date")
    private OffsetDateTime editDate;

}
