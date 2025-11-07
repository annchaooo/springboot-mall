package com.annchao.springboot_mall.dao;

import java.util.List;

import com.annchao.springboot_mall.dto.ProductQueryParam;
import com.annchao.springboot_mall.dto.ProductRequest;
import com.annchao.springboot_mall.model.product;


// 根據產品ID查詢產品
public interface ProductDao {

    List<product> getProducts(ProductQueryParam productQueryParam);

    product getProductId(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
