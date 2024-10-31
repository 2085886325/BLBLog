package com.buleng.service;

import com.buleng.domain.entity.ResponseResult;
import com.buleng.domain.entity.User;
import org.springframework.stereotype.Repository;

public interface BlogLoginService{

    ResponseResult login(User user);

    ResponseResult logout();
}
