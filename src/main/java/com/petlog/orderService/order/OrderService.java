package com.petlog.orderService.order;

import com.petlog.orderService.dto.CreateOrderRequestDto;
import com.petlog.orderService.dto.OrderItemDto;
import com.petlog.orderService.entity.Carts;
import com.petlog.orderService.dto.CartItemRequestDto;
import com.petlog.orderService.entity.Orders;
import com.petlog.productService.dto.DeleteCartRequestDto;
import com.petlog.orderService.dto.GetCartResponseDto;
import com.petlog.productService.dto.GetOrderSheetRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<GetCartResponseDto> getOrderSheet(int userId, GetOrderSheetRequestDto dto) {
        return orderRepository.getOrderSheet(userId, dto);
    }

    @Transactional
    public void createOrder (int userId, CreateOrderRequestDto dto) {

        Orders order = new Orders();

        order.setUserId(userId);
        order.setRecipient(dto.getRecipient());
        order.setPhone(dto.getPhone());
        order.setAddress(dto.getAddress());
        order.setTotalPrice(dto.getTotalPrice());
        orderRepository.createOrder(order);

        orderRepository.createOrderItem(order.getOrderId(), dto);

        for (OrderItemDto item : dto.getItems()) {
            orderRepository.updateProductStock(item.getProductId(), item.getQuantity());
        }
    }

}
