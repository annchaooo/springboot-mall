package com.annchao.springboot_mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.annchao.springboot_mall.constant.ProductCategory;
import com.annchao.springboot_mall.dto.ProductQueryParam;
import com.annchao.springboot_mall.dto.ProductRequest;
import com.annchao.springboot_mall.model.product;
import com.annchao.springboot_mall.service.ProductService;
import com.annchao.springboot_mall.util.Page;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;







@Validated
@RestController
public class ProductController {
    
    // Injecting ProductService to handle business logic and data retrieval
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<product>> getProducts(

        //查詢條件
        @RequestParam(required = false) ProductCategory category,
        @RequestParam(required = false) String search,

        // 排序 Sorting
        @RequestParam(defaultValue = "created_date") String orderBy,
        @RequestParam(defaultValue = "desc") String sort,

        //分頁 Pagination
        @RequestParam(defaultValue = "10") @Max(100) @Min(0) Integer limit,
        @RequestParam(defaultValue = "0")  @Min(0) Integer offset

    ) {

        ProductQueryParam productQueryParam = new ProductQueryParam();
        productQueryParam.setCategory(category);
        productQueryParam.setSearch(search);
        productQueryParam.setOrderBy(orderBy);
        productQueryParam.setSort(sort);
        productQueryParam.setLimit(limit);
        productQueryParam.setOffset(offset);


        // For now, just return an empty list

        // retrieve products based on query parameters from database
        List<product> productList = productService.getProducts(productQueryParam);

        // get total count of products matching the query parameters
        Integer total = productService.countProducts(productQueryParam);

        // encapsulate the result in a Page object
        Page<product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);
       
        // return 200 OK status with the page object as the response body
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
    
    @GetMapping("/products/{productId}")

    //ResponseEntity encapsulates HTTP response, including status code, headers, and body
    public ResponseEntity<product> getProductById(@PathVariable Integer productId) {
        // querying product by ID
        product product = productService.getProductById(productId);

        // if product is found, return 200 OK with product data; else return 404 Not Found
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

