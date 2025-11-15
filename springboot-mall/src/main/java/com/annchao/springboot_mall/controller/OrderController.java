package com.annchao.springboot_mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.annchao.springboot_mall.constant.ProductCategory;
import com.annchao.springboot_mall.dto.CreateOrderRequest;
import com.annchao.springboot_mall.dto.OrderQueryParam;
import com.annchao.springboot_mall.dto.ProductQueryParam;
import com.annchao.springboot_mall.model.Order;
import com.annchao.springboot_mall.model.product;
import com.annchao.springboot_mall.service.OrderService;
import com.annchao.springboot_mall.util.Page;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders (

        //查詢條件
        @PathVariable Integer userId,

        //分頁 Pagination
        @RequestParam(defaultValue = "10") @Valid @Max(1000) @Min(0) Integer limit,
        @RequestParam(defaultValue = "0")  @Valid @Min(0) Integer offset

    ) {

        OrderQueryParam orderQueryParam = new OrderQueryParam();
        orderQueryParam.setUserId(userId);
        orderQueryParam.setLimit(limit);
        orderQueryParam.setOffset(offset);


        // retrieve products based on query parameters from database
        List<Order> orderList = orderService.getOrders(orderQueryParam);

        // get total count of products matching the query parameters
        Integer count = orderService.countOrders(orderQueryParam);

        // encapsulate the result in a Page object
        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);
       
        // return 200 OK status with the page object as the response body
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
       
    
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
