package com.annchao.springboot_mall.dao;

import com.annchao.springboot_mall.dto.UserRegisterRequest;
import com.annchao.springboot_mall.model.User;

public interface UserDao {


    Integer createUser(UserRegisterRequest userRegisterRequest);
    
    User getUserById(Integer userId);

    User getUserByEmail(String email);

}
