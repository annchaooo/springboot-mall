package com.annchao.springboot_mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.annchao.springboot_mall.dao.ProductDao;
import com.annchao.springboot_mall.dto.ProductRequest;
import com.annchao.springboot_mall.model.product;
import com.annchao.springboot_mall.service.ProductService;

@Component
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductDao productDao;

    @Override
    public product getProductById(Integer productId) {
        return productDao.getProductId(productId);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }
    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        // TODO Auto-generated method stub
        productDao.updateProduct(productId, productRequest); 
    }

    @Override
    public void deleteProductById(Integer productId) {
        // TODO Auto-generated method stub
        productDao.deleteProductById(productId);
    }

    



}
