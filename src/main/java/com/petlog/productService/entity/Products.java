package com.petlog.productService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products extends CommonEntity {

    private int productId;
    private int userId; // 상품 등록한 유저
    private String name;
    private int price;
    private int quantity;

}