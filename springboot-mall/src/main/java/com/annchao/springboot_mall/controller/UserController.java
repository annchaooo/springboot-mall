package com.annchao.springboot_mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.annchao.springboot_mall.dto.UserLoginRequest;
import com.annchao.springboot_mall.dto.UserRegisterRequest;
import com.annchao.springboot_mall.model.User;
import com.annchao.springboot_mall.service.UserService;

import jakarta.validation.Valid;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // 註冊新帳號
    @PostMapping("users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {

        //創建帳號後，回傳新帳號的 userId
        Integer userId = userService.register(userRegisterRequest);
        
        // 透過 userId 取得使用者資料
        User user = userService.getUserById(userId);

        // 回傳 201 Created 狀態碼與使用者資料
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    //登入功能
    @PostMapping("users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        // 這裡可以實作登入邏輯，例如驗證使用者帳號密碼

        User user = userService.login(userLoginRequest);
        // 目前僅回傳 200 OK 狀態碼作為示範

        return ResponseEntity.status(HttpStatus.OK).body(user);
        
    }


}
