package com.annchao.springboot_mall.service.impl;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.annchao.springboot_mall.dao.OrderDao;
import com.annchao.springboot_mall.dao.ProductDao;
import com.annchao.springboot_mall.dao.UserDao;
import com.annchao.springboot_mall.dto.BuyItem;
import com.annchao.springboot_mall.dto.CreateOrderRequest;
import com.annchao.springboot_mall.model.Order;
import com.annchao.springboot_mall.model.OrderItem;
import com.annchao.springboot_mall.model.User;
import com.annchao.springboot_mall.model.product;
import com.annchao.springboot_mall.service.OrderService;


@Component
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderDao orderDao;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private UserDao userDao;


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
        
        // 檢查 UserId 是否存在
        User user = userDao.getUserById(userId);
        if (user == null){
            log.warn("User {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 訂單初始參數設定：
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();


        for (BuyItem buyItem: createOrderRequest.getBuyItemList()) {
            product product = productDao.getProductId(buyItem.getProductId());

            // 檢查 productId 是否存在 & stock 是否足夠
            if (product == null){
                log.warn("Product id {} does not exist", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            if (product.getStock() < buyItem.getQuantity()){
                
                log.warn("Product id {} stock is not enough, current stock: {}", buyItem.getProductId(), product.getStock());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 扣除商品庫存
            int newStock = product.getStock() - buyItem.getQuantity();
            productDao.updateProductStock(buyItem.getProductId(), newStock);
            

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
