package com.example.demo.dto;

import com.example.demo.entity.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    
    private Long id;
    private String orderNumber;
    private Long userId;
    private String userName;
    private OrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private List<OrderItemDTO> orderItems;
    private Timestamp createdAt;
}
