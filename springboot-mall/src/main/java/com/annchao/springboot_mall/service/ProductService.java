package com.annchao.springboot_mall.service;
import java.util.List;

import com.annchao.springboot_mall.dto.ProductQueryParam;
import com.annchao.springboot_mall.dto.ProductRequest;
import com.annchao.springboot_mall.model.product;


public interface ProductService {

    Integer countProducts(ProductQueryParam productQueryParam);

    // return data type; method name; parameters
    List<product> getProducts(ProductQueryParam productQueryParam);

    // Method to get product by its ID
    product getProductById(Integer productId);

    // Method to create a new product
    Integer createProduct(ProductRequest productRequest);

    // Method to update an existing product, void because no need to return anything
    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);


}
