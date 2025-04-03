package com.petlog.productService.product;

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

        int userId = extractUserIdFromToken(request);
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


    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseMessage> deleteProduct(@PathVariable("productId") int productId) {

        productService.deleteProduct(productId);

        ResponseMessage response = ResponseMessage.builder()
                .statusCode(200)
                .resultMessage("Product deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cart")
    public ResponseEntity<ResponseMessage> addsWishList(@RequestBody CartItemRequestDto dto, HttpServletRequest request){

        int userId = extractUserIdFromToken(request);

        productService.addWishList(userId, dto);

        ResponseMessage response = ResponseMessage.builder()
                .statusCode(200)
                .resultMessage("Product deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart")
    public ResponseEntity<ResponseMessage> getWishList(HttpServletRequest request){

        int userId = extractUserIdFromToken(request);

        List<GetWishListResponseDto> wishList= productService.getWishList(userId);

        ResponseMessage response = ResponseMessage.builder()
                .data(wishList)
                .statusCode(200)
                .resultMessage("Product deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cart")
    public ResponseEntity<ResponseMessage> updateWishList(@RequestBody CartItemRequestDto dto, HttpServletRequest request){

        int userId = extractUserIdFromToken(request);
        productService.updateWishList(dto, userId);

        ResponseMessage response = ResponseMessage.builder()
                .data(null)
                .statusCode(200)
                .resultMessage("Product deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cart")
    public ResponseEntity<ResponseMessage> deleteWishList(@RequestBody DeleteWishListRequestDto dto, HttpServletRequest request){

        int userId = extractUserIdFromToken(request);
        productService.deleteWishList(dto, userId);

        ResponseMessage response = ResponseMessage.builder()
                .data(null)
                .statusCode(200)
                .resultMessage("Product deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    private int extractUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        token = token.replace("Bearer ", ""); // "Bearer " 제거
        Claims claims = jwtUtil.getUserInfoFromToken(token); // JWT에서 클레임 가져오기

        Object userIdObject = claims.get("userId");
        if (userIdObject == null) {
            throw new RuntimeException("User ID not found in token claims");
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdObject.toString()); // 숫자로 변환
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid user ID format in token");
        }

        return userId;
    }

}
