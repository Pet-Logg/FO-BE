package com.petlog.productService.dto;

import com.petlog.productService.entity.Products;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class getProductsResponseDto extends Products {
    private List<String> imgUrl;
}
