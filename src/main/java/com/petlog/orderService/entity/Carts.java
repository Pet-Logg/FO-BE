package com.petlog.orderService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carts {

    private int id;
    private int userId;
    private int productId;
    private int quantity;

}
