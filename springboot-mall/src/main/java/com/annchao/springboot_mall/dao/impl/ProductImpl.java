package com.annchao.springboot_mall.dao.impl;
import com.annchao.springboot_mall.dao.ProductDao;
import com.annchao.springboot_mall.dto.ProductRequest;
import com.annchao.springboot_mall.model.product;
import com.annchao.springboot_mall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class ProductImpl implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public product getProductId(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, " +
                "description, created_date, last_modified_date " +
                "FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        // parameterized query, using product row mapper to map the result set (data) to java object
        List<product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if (productList.size() > 0) {
            return productList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";
        
        // map to hold the parameters for the SQL query from the productRequest object
        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());            
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Timestamp(System.currentTimeMillis());
        map.put("createdDate", now);
        map.put("lastModifiedDate", now); 
        
        // to get the auto-generated primary key after insertion
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map) , keyHolder);

        Integer productId = keyHolder.getKey().intValue();
        return productId;
    }
x
    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :productName, category = :category, image_url = :imageUrl, " +
                "price = :price, stock = :stock, description = :description, last_modified_date = :lastModifiedDate " +
                "WHERE product_id = :productId";        

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());       
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        map.put("lastModifiedDate", new Timestamp(System.currentTimeMillis()));

        namedParameterJdbcTemplate.update(sql, map);
    }


}


