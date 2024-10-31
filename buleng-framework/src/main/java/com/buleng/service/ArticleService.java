package com.buleng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.buleng.domain.entity.Article;
import com.buleng.domain.entity.ResponseResult;

public interface ArticleService extends IService<Article> {


    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);
}
