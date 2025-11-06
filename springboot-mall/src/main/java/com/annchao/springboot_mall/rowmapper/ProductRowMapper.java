package com.annchao.springboot_mall.rowmapper;
import com.annchao.springboot_mall.constant.ProductCategory;
import com.annchao.springboot_mall.model.product;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


// this class maps the result set from the database to the product model (database -> java object)
public class ProductRowMapper implements RowMapper<product> {

    @Override
        public product mapRow(ResultSet resultSet, int i) throws SQLException {
        product product = new product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setCategory(ProductCategory.valueOf(resultSet.getString("category")));

        product.setImageUrl(resultSet.getString("image_url"));
        product.setPrice(resultSet.getInt("price"));
        product.setStock(resultSet.getInt("stock"));
        product.setDescription(resultSet.getString("description"));
        product.setCreatedDate(resultSet.getTimestamp("created_date"));
        product.setLastModifiedDate(resultSet.getTimestamp("last_modified_date"));

        return product;
    }
}
