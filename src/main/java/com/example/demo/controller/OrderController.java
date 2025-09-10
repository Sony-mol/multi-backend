package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order APIs", description = "Manage orders and payments")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new order")
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @RequestParam Long userId,
            @RequestBody List<OrderItemDTO> orderItems,
            @RequestParam String shippingAddress) {
        return ResponseEntity.ok(orderService.createOrder(userId, orderItems, shippingAddress));
    }

    @Operation(summary = "Process payment for an order")
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<String> processPayment(
            @PathVariable Long orderId,
            @RequestParam String paymentMethod) {
        orderService.processPayment(orderId, paymentMethod);
        return ResponseEntity.ok("Payment processed successfully");
    }

    @Operation(summary = "Get order by ID")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @Operation(summary = "Get orders by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @Operation(summary = "Get initial 4 products for first purchase")
    @GetMapping("/initial-products")
    public ResponseEntity<String> getInitialProductsInfo() {
        return ResponseEntity.ok("Initial 4 products available for 1000rs total (250rs each): " +
                "1. Starter Package, 2. Growth Package, 3. Success Package, 4. Elite Package");
    }
}
