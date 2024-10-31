package com.buleng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buleng.domain.entity.User;
import org.springframework.stereotype.Repository;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-15 15:02:38
 */

//加上这个注解防止自动注入报错
@Repository(value = "UserMapper")
public interface UserMapper extends BaseMapper<User> {

}

