package com.annchao.springboot_mall.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.annchao.springboot_mall.model.OrderItem;

public class OrderItemRowMapper implements RowMapper<OrderItem>{

    @Override
    public OrderItem mapRow(ResultSet resultSet, int i) throws SQLException {

        // row mapper的功能是 取得 DAO impl 中 SELECT 出來的欄位，都可以使用
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(resultSet.getInt("order_id"));
        orderItem.setOrderItemId(resultSet.getInt("order_item_id"));
        orderItem.setProductId(resultSet.getInt("product_id"));
        orderItem.setQuantity(resultSet.getInt("quantity"));
        orderItem.setAmount(resultSet.getInt("amount"));

        // OrderDaoImpl 有回傳商品名稱/商品照片給前端，因此這裡需要加這個資訊
        //需要擴充原有的 OrderItem(到 OrderItem class 新增變數, join其他 table 會遇到的問題)
        orderItem.setProductName(resultSet.getString("product_name"));
        orderItem.setImageUrl(resultSet.getString("image_url"));

        return orderItem;

    }

}
