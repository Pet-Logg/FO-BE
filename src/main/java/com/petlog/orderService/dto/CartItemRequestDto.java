package com.petlog.orderService.dto;

import lombok.Data;

@Data
public class CartItemRequestDto {
    private int productId;
    private int quantity;
}
