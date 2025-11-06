package com.annchao.springboot_mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.annchao.springboot_mall.dto.ProductRequest;
import com.annchao.springboot_mall.model.product;
import com.annchao.springboot_mall.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
public class ProductController {
    
    // Injecting ProductService to handle business logic and data retrieval
    @Autowired
    private ProductService productService;
    
    @GetMapping("/products/{productId}")

    //ResponseEntity encapsulates HTTP response, including status code, headers, and body
    public ResponseEntity<product> getProductById(@PathVariable Integer productId) {
        // querying product by ID
        product product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
         }
        }

    
    @PostMapping("/products")
    // Method to create a new product but currently not using product model directly because we want to validate the input data first
    // so we will use ProductRequest DTO instead
    public ResponseEntity<product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        // TODO Auto-generated method stub
        Integer productId = productService.createProduct(productRequest);

        // retrieve the newly created product infromation using the returned productId
        product product = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<product> updateProduct(@PathVariable Integer productId, @RequestBody @Valid ProductRequest productRequest) {
        
        // query the product by ID to check if it exists
        product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // if product exists, proceed to update
        productService.updateProduct(productId, productRequest);

        product updateProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        // query the product by ID to check if it exists
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}

