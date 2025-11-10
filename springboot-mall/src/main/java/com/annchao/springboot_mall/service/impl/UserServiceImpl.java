package com.annchao.springboot_mall.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.annchao.springboot_mall.dao.UserDao;
import com.annchao.springboot_mall.dto.UserLoginRequest;
import com.annchao.springboot_mall.dto.UserRegisterRequest;
import com.annchao.springboot_mall.model.User;
import com.annchao.springboot_mall.service.UserService;



@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    // 創建 logger instance, local variable can be used here with "final" keyword
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {

        // 創建 logger instance, local variable can be used here with "final" keyword

        // 檢查註冊的帳號：一組email 只能註冊一個帳號
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        if (user != null) {
            // 已經有相同email的帳號，無法註冊
            log.warn("該 Email {} 已經被註冊了", userRegisterRequest.getEmail() );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        //創建帳號
        return userDao.createUser(userRegisterRequest);

    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        
        // 透過 email 取得資料庫中的使用者資料
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        //檢查 User 是否存在
        if (user == null) {
            log.warn("該 Email {} 尚未註冊", userLoginRequest.getEmail() );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found");

        }
        // 檢查密碼是否正確
        if (!user.getPassword().equals(userLoginRequest.getPassword())) {

            log.warn("Email {} 的密碼不正確", userLoginRequest.getEmail() );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
            
        }   else{
            
            log.info("使用者 {} 登入成功", userLoginRequest.getEmail() );
            // 登入成功，回傳使用者資料
             return user;

        }


    }   


}
