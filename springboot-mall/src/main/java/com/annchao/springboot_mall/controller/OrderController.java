package com.annchao.springboot_mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.annchao.springboot_mall.dto.CreateOrderRequest;
import com.annchao.springboot_mall.model.Order;
import com.annchao.springboot_mall.service.OrderService;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 訂單功能是user功能的附屬品，因此寫在他的路徑下面
    @PostMapping("users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                        @RequestBody @Valid CreateOrderRequest createOrderRequest) {
        // TODO: Implement order creation logic
        Integer orderId = orderService.createOrder(userId, createOrderRequest);
        
        // 取得訂單數據，作為 Response Body
        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);

    }

}
