package com.petlog.productService.product;

import com.petlog.orderService.dto.GetCartResponseDto;
import com.petlog.productService.dto.*;
import com.petlog.userService.dto.ResponseMessage;
import com.petlog.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    // 제품 생성
    @PostMapping
    public ResponseEntity<ResponseMessage> createProduct(@ModelAttribute CreateProductRequestDto createProductDto, HttpServletRequest request) {

        int userId = jwtUtil.extractUserIdFromToken(request);
        productService.createProduct(createProductDto, userId);

        ResponseMessage response = ResponseMessage.builder()
                .data(null)
                .statusCode(201)
                .resultMessage("Product created successfully")
                .build();
        return ResponseEntity.status(201).body(response);
    }

    // 모든 상품 조회
    @GetMapping("/products")
    public ResponseEntity<ResponseMessage> getAllProducts() {

        List<GetProductsResponseDto> products = productService.getAllProducts();

        ResponseMessage response = ResponseMessage.builder()
                .data(products)
                .statusCode(200)
                .resultMessage("Success")
                .build();
        return ResponseEntity.ok(response);
    }

    // productId로 상품 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ResponseMessage> getProductById(@PathVariable int productId) {

        GetProductsResponseDto product = productService.getProductById(productId);

        ResponseMessage response = ResponseMessage.builder()
                .statusCode(200)
                .resultMessage("Success")
                .data(product)
                .build();
        return ResponseEntity.ok(response);
    }

    // 상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<ResponseMessage> updateProduct(@PathVariable("productId") int productId, UpdateProductRequestDto dto) {

        productService.updateProduct(productId, dto);

        ResponseMessage response = ResponseMessage.builder()
                .data(null)
                .statusCode(200)
                .resultMessage("Product updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseMessage> deleteProduct(@PathVariable("productId") int productId) {

        productService.deleteProduct(productId);

        ResponseMessage response = ResponseMessage.builder()
                .statusCode(200)
                .resultMessage("Product deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

}
