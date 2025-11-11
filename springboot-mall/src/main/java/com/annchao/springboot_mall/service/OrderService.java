package com.annchao.springboot_mall.service;

import com.annchao.springboot_mall.dto.CreateOrderRequest;
import com.annchao.springboot_mall.model.Order;

public interface OrderService {

    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);



}
