package com.petlog.orderService.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private int productId;
    private int quantity;
}
