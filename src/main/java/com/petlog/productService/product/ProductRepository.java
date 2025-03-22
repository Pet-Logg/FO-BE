package com.petlog.productService.product;

import com.petlog.productService.entity.ProductImages;
import com.petlog.productService.entity.Products;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductRepository {

    void createProduct(Products product);

    void insertProductImage(ProductImages productImg);

    List<Products> getAllProducts();
}