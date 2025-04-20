package com.petlog.orderService.entity;

import com.petlog.common.entity.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItems extends CommonEntity {

    private int OrderItemsId;
    private int orderId;
    private int productId;
    private int quantity;

}
