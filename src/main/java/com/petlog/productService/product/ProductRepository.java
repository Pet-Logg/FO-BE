package com.petlog.productService.product;

import com.petlog.productService.dto.CreateProductDto;
import com.petlog.productService.dto.getProductsResponseDto;
import com.petlog.productService.entity.ProductImages;
import com.petlog.productService.entity.Products;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductRepository {

    // 상품 등록
    void createProduct(Products product);

    // 상품 이미지 등록
    void insertProductImage(ProductImages productImg);

    // 모든 상품 조회
    List<getProductsResponseDto> getAllProducts();

    // productId로 상품 조회
    getProductsResponseDto getProductById(int productId);

    // 상품 삭제
    void deleteProduct(int productId);

    // 상품 수정
    void updateProduct(Map<String, Object> params);

    List<String> findS3KeysByProductId(int productId);

    void deleteImgByProductId(int productId);

    void insertProductImage2(List<ProductImages> imageEntities);
}