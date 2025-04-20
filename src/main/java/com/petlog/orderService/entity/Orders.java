package com.petlog.orderService.entity;

import com.petlog.common.entity.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders extends CommonEntity {
    private int orderId;
    private int userId;
    private String recipient; // 수신자
    private String phone;
    private String address;
    private int totalPrice;
}
