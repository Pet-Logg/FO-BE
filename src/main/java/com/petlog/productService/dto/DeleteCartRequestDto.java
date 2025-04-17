package com.petlog.productService.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteCartRequestDto {
    private List<Integer> selectedItems;
}
