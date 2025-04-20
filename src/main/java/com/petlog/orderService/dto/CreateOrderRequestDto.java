package com.petlog.orderService.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequestDto {
    private String recipient;   // 수취인
    private String phone;
    private String address;
    private int totalPrice;
    private List<OrderItemDto> items;
}
