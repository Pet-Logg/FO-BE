package com.petlog.productService.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteWishListRequestDto {
    private List<Integer> selectedItems;
}
