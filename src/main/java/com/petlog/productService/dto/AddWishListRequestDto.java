package com.petlog.productService.dto;

import lombok.Data;

@Data
public class AddWishListRequestDto {
    private int productId;
    private int quantity;
}
