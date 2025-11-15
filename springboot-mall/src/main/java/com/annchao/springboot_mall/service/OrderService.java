package com.annchao.springboot_mall.service;

import java.util.List;

import com.annchao.springboot_mall.dto.CreateOrderRequest;
import com.annchao.springboot_mall.dto.OrderQueryParam;
import com.annchao.springboot_mall.model.Order;

public interface OrderService {

    List<Order> getOrders(OrderQueryParam orderQueryParam);

    Integer countOrders(OrderQueryParam orderQueryParam);

    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);



}
