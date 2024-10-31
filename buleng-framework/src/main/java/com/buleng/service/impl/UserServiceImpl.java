package com.buleng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.buleng.domain.entity.User;
import com.buleng.mapper.UserMapper;
import com.buleng.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-11 17:43:19
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
