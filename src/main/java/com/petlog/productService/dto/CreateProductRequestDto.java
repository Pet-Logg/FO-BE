package com.petlog.productService.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateProductRequestDto {

    private String name;

    private List<MultipartFile> productImg;

    private int price;

    private int quantity;

}
