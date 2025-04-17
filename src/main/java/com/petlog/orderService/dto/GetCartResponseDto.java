package com.petlog.orderService.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetCartResponseDto {
    private int id;
    private int productId;
    private String name;
    private int price;
    private int quantity;
    private List<String> imgUrl;
}
