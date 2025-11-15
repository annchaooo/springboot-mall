package com.annchao.springboot_mall.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.annchao.springboot_mall.dao.OrderDao;
import com.annchao.springboot_mall.dto.OrderQueryParam;
import com.annchao.springboot_mall.model.Order;
import com.annchao.springboot_mall.model.OrderItem;
import com.annchao.springboot_mall.model.product;
import com.annchao.springboot_mall.rowmapper.OrderItemRowMapper;
import com.annchao.springboot_mall.rowmapper.OrderRowMapper;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countOrders(OrderQueryParam orderQueryParam){
        String sql = "SELECT COUNT(*) FROM `order` WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        // add filtering conditions if provided in orderQueryParam
        sql = addFilteringSql(sql, map, orderQueryParam);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return count;

    }

    @Override
    public List<Order> getOrders(OrderQueryParam orderQueryParam){
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +  
        "FROM `order` WHERE 1=1";

        Map<String, Object> map = new HashMap<>();
        
        // add filtering conditions if provided in orderQueryParam
        sql = addFilteringSql(sql, map, orderQueryParam);

        // 排序
        sql += " ORDER BY created_date DESC";
        
        // add pagination
        sql += " LIMIT :limit OFFSET :offset";
        map.put("limit", orderQueryParam.getLimit());
        map.put("offset", orderQueryParam.getOffset()); 

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());
        return orderList;
        }

    @Override
    public Order getOrderById(Integer orderId){

        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
        "FROM `order` WHERE order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        // parameterized query, using product row mapper to map the result set (data) to java object
        List<Order> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        if (orderItemList.size() > 0) {
            return orderItemList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId){
        // 同時取得一些商品名稱/商品圖片等紀錄（對消費者而言重要的資訊）
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url " + 
        "FROM order_item as oi " +
        "LEFT JOIN product as p ON oi.product_id = p.product_id " +
        "WHERE oi.order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());


        return orderItemList;

    }



    @Override
    public void createOrderItems(Integer orderId, List<OrderItem>orderItemList){

        // 透過 for loop 一條一條把order item加到 table, 效率較低
        // for(OrderItem orderItem: orderItemList){
        //     String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount)" +
        //         "VALUES (:orderId, :productId, :quantity, :amount)";

        //     Map<String, Object> map = new HashMap<>();
        //     map.put("orderId", orderId);
        //     map.put("productId", orderItem.getProductId());
        //     map.put("quantity", orderItem.getQuantity());
        //     map.put("amount", orderItem.getAmount());

        //     // same logic with update product, (as reference)
        //     namedParameterJdbcTemplate.update(sql, map);

        // }

        //透過batchUpdate 一次性加入，效率較高

        String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount) " +
                "VALUES (:orderId, :productId, :quantity, :amount)";


        // 建立批次參數
        SqlParameterSource[] batchParams = orderItemList.stream()
            .map(oi -> new MapSqlParameterSource()
                .addValue("orderId", orderId)
                .addValue("productId", oi.getProductId())
                .addValue("quantity", oi.getQuantity())
                .addValue("amount", oi.getAmount()))
            .toArray(SqlParameterSource[]::new);

        // 一次性批次寫入
        int[] counts = namedParameterJdbcTemplate.batchUpdate(sql, batchParams);
        // 可選：檢查 counts 長度或成功筆數
        
    }

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount){

        String sql = "INSERT INTO `order` (user_id, total_amount, created_date, last_modified_date)" + 
        "VALUES (:userId, :totalAmount, :createdDate, :lastModifiedDate)";
        
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);

        Date now = new Timestamp(System.currentTimeMillis());
        map.put("createdDate", now);
        map.put("lastModifiedDate", now); 

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map) , keyHolder);
        int orderId = keyHolder.getKey().intValue();
        return orderId;

    }

    private String addFilteringSql(String sql, Map<String, Object> map, OrderQueryParam orderQueryParam) {
        // No additional filtering conditions for now

        if (orderQueryParam.getUserId() != null) {
            sql += " AND user_id = :userId";
            map.put("userId", orderQueryParam.getUserId());
        }
        return sql;
    }

}
