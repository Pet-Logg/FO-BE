package com.petlog.productService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImages {

    private int productImageId;
    private int productId;
    private String imgUrl;
    private String s3Key; // S3 내부 파일 경로
}
