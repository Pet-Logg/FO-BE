package com.petlog.productService.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateProductRequestDto {

    private String name;

    private List<MultipartFile> productImg; // 새로 업로드한 이미지

    private int price;

    private int quantity;

    private List<String> S3Key; // 기존 이미지 S3 Key

}
