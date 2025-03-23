package com.petlog.productService.product;

import com.petlog.productService.dto.CreateProductDto;
import com.petlog.productService.dto.getProductsResponseDto;
import com.petlog.productService.entity.Products;
import com.petlog.userService.dto.ResponseMessage;
import com.petlog.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    // 제품 생성
    @PostMapping
    public ResponseEntity<ResponseMessage> createProduct(@ModelAttribute CreateProductDto createProductDto, HttpServletRequest request) {

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
    public ResponseEntity<ResponseMessage> getAllProducts(HttpServletRequest request) {

        List<getProductsResponseDto> products = productService.getAllProducts();
        System.out.println("products : " + products);
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

        getProductsResponseDto product = productService.getProductById(productId);

        ResponseMessage response = ResponseMessage.builder()
                .statusCode(200)
                .resultMessage("Success")
                .data(product)
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{productId}")
    public ResponseEntity<ResponseMessage> updateProduct(@PathVariable("productId") int productId, CreateProductDto dto) {

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
//
//
//    /**
//     * 특정 상품 ID 목록으로 상품 정보 조회
//     *
//     * @param productIds 상품 ID 목록
//     * @return 상품 정보 목록
//     */
//    @GetMapping
//    public ResponseEntity<List<ProductDto>> getProductsByIds(@RequestParam("ids") List<Integer> productIds) {
//        // ProductService를 통해 상품 정보 조회
//        List<Product> products = productService.getProductsByIds(productIds);
//        // Product 엔티티를 ProductDto(마이크로 서비스 간 사용하는 DTO)로 변환
//        List<ProductDto> productDtos = products.stream()
//                .map(product -> new ProductDto(product.getProductId(), product.getName(), product.getPrice(), product.getDescription(), product.getExposeYsno()))
//                .collect(Collectors.toList());
//        // userservice에 응답
//        return ResponseEntity.ok(productDtos);
//    }
//
//
//    // Order-Service : 재고 업데이트
//    // rabbitMQ로 대체
////    @PutMapping("/updateStock")
////    public void updateStock(@RequestBody CreateOrderDto createOrderDto) {
////        productService.updateStock(createOrderDto);
////    }
//
//
//    // Order-Service : 재고 되돌리기
//    @PutMapping("/restoreStock")
//    public void restoreStock(@RequestBody CreateOrderDto createOrderDto) {
//        productService.restoreStock(createOrderDto);
//    }
//
//
//    // Order-Service : 가격 가져오기
//    // rabbitMQ로 대체
////    @GetMapping("/getPrice")
////    public int getProductPrice(@RequestParam("productId") int productId) {
////        return productService.getProductPrice(productId);
////    }

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
