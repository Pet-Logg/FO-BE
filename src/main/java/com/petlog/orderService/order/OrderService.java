package com.petlog.orderService.order;

import com.petlog.orderService.entity.Carts;
import com.petlog.orderService.dto.CartItemRequestDto;
import com.petlog.productService.dto.DeleteCartRequestDto;
import com.petlog.orderService.dto.GetCartResponseDto;
import com.petlog.productService.dto.GetOrderSheetRequestDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void addCart(int userId, CartItemRequestDto dto){
        Carts cart = new Carts();
        cart.setUserId(userId);
        cart.setProductId(dto.getProductId());
        cart.setQuantity(dto.getQuantity());

        orderRepository.addCart(cart);
    }

    public List<GetCartResponseDto> getCart(int userId){
        return orderRepository.getCart(userId);
    }

    public void updateCart(int userId, CartItemRequestDto dto){
        orderRepository.updateCart(userId, dto);
    }

    public void deleteCart(int userId, DeleteCartRequestDto dto){
        orderRepository.deleteCart(userId, dto);
    }

    public List<GetCartResponseDto> getOrderSheet(GetOrderSheetRequestDto dto, int userId) {
        return orderRepository.getOrderSheet(userId, dto);
    }
}
