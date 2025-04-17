package com.petlog.productService.product;

import com.petlog.productService.dto.*;
import com.petlog.productService.entity.Carts;
import com.petlog.productService.entity.ProductImages;
import com.petlog.productService.entity.Products;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductRepository {

    // 상품 등록
    void createProduct(Products product);

    // 상품 이미지 등록
    void insertProductImage(List<ProductImages> imageEntities);
    // 모든 상품 조회
    List<GetProductsResponseDto> getAllProducts();

    // productId로 상품 조회
    GetProductsResponseDto getProductById(int productId);

    // 상품 삭제
    void deleteProduct(int productId);

    // 상품 수정
    void updateProduct(Map<String, Object> params);

    // S3 이미지 키 조
    List<String> findS3KeysByProductId(int productId);

    // productId로 이미지 삭제하기
    void deleteImgByProductId(int productId);

    // S3Key로 상품 이미지 삭제
    void deleteImgByS3Keys(List<String> toDeleteS3Keys);

    // 장바구니 추가
    void addCart(Carts cart);

    // 장바구니 조회
    List<GetCartResponseDto> getCart(int userId);

    // 장바구니 수정
    void updateCart(
            @Param("dto") CartItemRequestDto dto,
            @Param("userId") int userId
    );

    // 장바구니에서 상품삭제
    void deleteCart(@Param("dto") DeleteCartRequestDto dto,
                        @Param("userId") int userId
    );
}