package com.petlog.orderService.order;

import com.petlog.orderService.dto.CartItemRequestDto;
import com.petlog.orderService.dto.CreateOrderRequestDto;
import com.petlog.productService.dto.DeleteCartRequestDto;
import com.petlog.orderService.dto.GetCartResponseDto;
import com.petlog.productService.dto.GetOrderSheetRequestDto;
import com.petlog.userService.dto.ResponseMessage;
import com.petlog.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final JwtUtil jwtUtil;
    private final OrderService orderService;

    // 장바구니 추가
    @PostMapping("/cart")
    public ResponseEntity<ResponseMessage> addCart(@RequestBody CartItemRequestDto dto, HttpServletRequest request) {

        int userId = jwtUtil.extractUserIdFromToken(request);
        orderService.addCart(userId, dto);

        ResponseMessage response = ResponseMessage.builder()
                .statusCode(200)
                .resultMessage("Product deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }


    // 장바구니 조회
    @GetMapping("/cart")
    public ResponseEntity<ResponseMessage> getCart(HttpServletRequest request) {

        int userId = jwtUtil.extractUserIdFromToken(request);
        List<GetCartResponseDto> cart = orderService.getCart(userId);

        ResponseMessage response = ResponseMessage.builder()
                .data(cart)
                .statusCode(200)
                .resultMessage("Product deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // 장바구니 수정
    @PutMapping("/cart")
    public ResponseEntity<ResponseMessage> updateCart(@RequestBody CartItemRequestDto dto, HttpServletRequest request) {

        int userId = jwtUtil.extractUserIdFromToken(request);
        orderService.updateCart(userId, dto);

        ResponseMessage response = ResponseMessage.builder()
                .data(null)
                .statusCode(200)
                .resultMessage("Product deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // 장바구니 삭제
    @DeleteMapping("/cart")
    public ResponseEntity<ResponseMessage> deleteCart(@RequestBody DeleteCartRequestDto dto, HttpServletRequest request) {

        int userId = jwtUtil.extractUserIdFromToken(request);
        orderService.deleteCart(userId, dto);

        ResponseMessage response = ResponseMessage.builder()
                .data(null)
                .statusCode(200)
                .resultMessage("Product delete successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // 주문서 목록 조회
    @PostMapping("/getOrderSheet")
    private ResponseEntity<ResponseMessage> getOrderSheet (@RequestBody GetOrderSheetRequestDto dto, HttpServletRequest request) {

        int userId = jwtUtil.extractUserIdFromToken(request);
        List<GetCartResponseDto> orderSheetItems = orderService.getOrderSheet(userId, dto);

        ResponseMessage response = ResponseMessage.builder()
                .data(orderSheetItems)
                .statusCode(201)
                .resultMessage("Diary create successfully")
                .build();

        return ResponseEntity.status(201).body(response);
    }

    // 주문 생성과 재고 감소
    @PostMapping
    private ResponseEntity<ResponseMessage> createOrder (@RequestBody CreateOrderRequestDto dto, HttpServletRequest request) {
        int userId = jwtUtil.extractUserIdFromToken(request);

        orderService.createOrder(userId, dto);

        ResponseMessage response = ResponseMessage.builder()
                .data(null)
                .statusCode(200)
                .resultMessage("Order create successfully")
                .build();
        return ResponseEntity.ok(response);
    }

}
