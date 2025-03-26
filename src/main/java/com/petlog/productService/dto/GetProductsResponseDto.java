package com.petlog.productService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductsResponseDto {
    private int productId;
    private int userId; // 상품 등록한 유저
    private String name;
    private int price;
    private int quantity;
    private List<String> imgUrl;
}
