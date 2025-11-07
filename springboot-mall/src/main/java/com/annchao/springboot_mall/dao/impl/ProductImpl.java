package com.annchao.springboot_mall.dao.impl;
import com.annchao.springboot_mall.dao.ProductDao;
import com.annchao.springboot_mall.dto.ProductQueryParam;
import com.annchao.springboot_mall.dto.ProductRequest;
import com.annchao.springboot_mall.model.product;
import com.annchao.springboot_mall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

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
    public Integer countProducts(ProductQueryParam productQueryParam) {
        String sql = "SELECT COUNT(*) FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        //查詢條件
        sql = addFilteringSql(sql, map, productQueryParam);

        //Integer.class to convert the result to Integer
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;
    }

    @Override
    public List<product> getProducts(ProductQueryParam productQueryParam) {
        // SQL query to select all products
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, " +
                "description, created_date, last_modified_date " +
                "FROM product WHERE 1=1"; // WHERE 1=1 is a placeholder for dynamic conditions

        // using map to hold parameters for the SQL query
        Map<String, Object> map = new HashMap<>();

        //查詢條件
        sql = addFilteringSql(sql, map, productQueryParam);

        // 排序 Sorting
    
        sql += " ORDER BY " + productQueryParam.getOrderBy() + " " + productQueryParam.getSort();
        
        // 分頁 Paginationㄋ
        sql += " LIMIT :limit OFFSET :offset";

        // add pagination parameters, to avoid  SQL injection, we use parameterized query
        map.put("limit", productQueryParam.getLimit());
        map.put("offset", productQueryParam.getOffset());

        List<product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        return productList;
        
    }


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

    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    private String addFilteringSql(String sql, Map<String, Object> map, ProductQueryParam productQueryParam) {
        //查詢條件
        if (productQueryParam.getCategory() != null) {
            sql += " AND category = :category";
            map.put("category", productQueryParam.getCategory().name());
        }

        if (productQueryParam.getSearch() != null) {
            sql += " AND product_name LIKE :search";
            map.put("search", "%" + productQueryParam.getSearch() + "%");
        }
        return sql;
    }





}


