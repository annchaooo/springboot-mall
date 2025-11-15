package com.annchao.springboot_mall.dao;

import java.util.List;

import com.annchao.springboot_mall.dto.OrderQueryParam;
import com.annchao.springboot_mall.model.Order;
import com.annchao.springboot_mall.model.OrderItem;

public interface OrderDao {

    List<Order> getOrders(OrderQueryParam orderQueryParam);

    Integer countOrders(OrderQueryParam orderQueryParam);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    Integer createOrder(Integer userId, Integer totalAmount);

    // 沒有返回類型
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);


}
