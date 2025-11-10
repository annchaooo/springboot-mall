package com.annchao.springboot_mall.dao.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.annchao.springboot_mall.dao.UserDao;
import com.annchao.springboot_mall.dto.UserRegisterRequest;
import com.annchao.springboot_mall.model.User;
import com.annchao.springboot_mall.rowmapper.UserRowMapper;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        // Todo Auto-generated method stub
        String sql = "INSERT INTO user (email, password, created_date, last_modified_date) " +
                "VALUES (:email, :password, NOW(), NOW())";

        Map<String, Object> map = new HashMap<>();
        
        map.put("email", userRegisterRequest.getEmail());   
        map.put("password", userRegisterRequest.getPassword());

        Date now = new Date(System.currentTimeMillis());
        map.put("created_date", now);
        map.put("last_modified_date", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int userId = keyHolder.getKey().intValue();
        return userId;

    }

    @Override
    public User getUserById(Integer userId) {
        // Todo Auto-generated method stub
        String sql = "SELECT user_id, password, email, created_date, last_modified_date " +
                "FROM user WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());  

        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }

    }

}
