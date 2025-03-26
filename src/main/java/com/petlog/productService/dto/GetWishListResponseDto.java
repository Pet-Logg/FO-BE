package com.petlog.productService.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetWishListResponseDto {
    private int id;
    private int productId;
    private String name;
    private int price;
    private int quantity;
    private List<String> imgUrl;
}
