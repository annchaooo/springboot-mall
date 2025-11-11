package com.annchao.springboot_mall.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;


public class CreateOrderRequest {

    // BuyItemList 對應到前端傳入的Key值，但因為前端傳入的是json object,
    //所以還要再加一個class 接住 json object
    @NotEmpty
    private List<BuyItem>buyItemList;


    public List<BuyItem> getBuyItemList() {
        return this.buyItemList;
    }

    public void setBuyItemList(List<BuyItem> buyItemList) {
        this.buyItemList = buyItemList;
    }

}