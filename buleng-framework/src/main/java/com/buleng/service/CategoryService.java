package com.buleng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.buleng.domain.entity.Category;
import com.buleng.domain.entity.ResponseResult;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-03-12 18:37:04
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}

