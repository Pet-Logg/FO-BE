package com.petlog.orderService.order;

import com.petlog.orderService.dto.CreateOrderRequestDto;
import com.petlog.orderService.entity.Carts;
import com.petlog.orderService.dto.CartItemRequestDto;
import com.petlog.orderService.entity.Orders;
import com.petlog.productService.dto.DeleteCartRequestDto;
import com.petlog.orderService.dto.GetCartResponseDto;
import com.petlog.productService.dto.GetOrderSheetRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderRepository {
    // 장바구니 추가
    void addCart(Carts cart);

    // 장바구니 조회
    List<GetCartResponseDto> getCart(int userId);

    // 장바구니 수정
    void updateCart(
            @Param("userId") int userId,
            @Param("dto") CartItemRequestDto dto
    );

    // 장바구니에서 상품삭제
    void deleteCart(
            @Param("userId") int userId,
            @Param("dto") DeleteCartRequestDto dto
    );


    // 주문서 상품 조회
    List<GetCartResponseDto> getOrderSheet(
            @Param("userId") int userId,
            @Param("dto") GetOrderSheetRequestDto dto
    );

    // 주문 생성
    void createOrder(Orders order);

    // 주문 아이템 생성
    void createOrderItem(
            @Param("orderId") int orderId,
            @Param("dto") CreateOrderRequestDto dto
    );
}