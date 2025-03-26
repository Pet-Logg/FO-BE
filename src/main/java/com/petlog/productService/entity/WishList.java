package com.petlog.productService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishList {

    private int id;
    private int userId;
    private int productId;
    private int quantity;

}
