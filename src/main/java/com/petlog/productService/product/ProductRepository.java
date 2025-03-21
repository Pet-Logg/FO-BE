package com.petlog.productService.product;

import com.petlog.productService.entity.ProductImages;
import com.petlog.productService.entity.Products;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductRepository {

    void createProduct(Products product);

    void insertProductImage(ProductImages productImg);

}