package com.annchao.springboot_mall.service.impl;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.annchao.springboot_mall.dao.OrderDao;
import com.annchao.springboot_mall.dao.ProductDao;
import com.annchao.springboot_mall.dto.BuyItem;
import com.annchao.springboot_mall.dto.CreateOrderRequest;
import com.annchao.springboot_mall.model.Order;
import com.annchao.springboot_mall.model.OrderItem;
import com.annchao.springboot_mall.model.product;
import com.annchao.springboot_mall.service.OrderService;


@Component
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderDao orderDao;


    @Autowired
    private ProductDao productDao;


    @Override
    public Order getOrderById(Integer orderId){
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        // 合併 order, orderItemList --> 擴充 order class --> 把 orderItemList （變數） 加到 order 這個 class
        order.setOrderItemList(orderItemList);

        return order;
        

    }

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest){

        // 計算訂單總花費
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();


        for (BuyItem buyItem: createOrderRequest.getBuyItemList()) {
            product product = productDao.getProductId(buyItem.getProductId());

            //計算總價
            int amount = buyItem.getQuantity()*product.getPrice();
            totalAmount = totalAmount + amount;
            
            //轉換 buyItem to orderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }


        //創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
       

    }

}
