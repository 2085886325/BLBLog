package com.buleng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.buleng.domain.entity.Comment;
import com.buleng.domain.entity.ResponseResult;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-04-11 16:30:07
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize);

}

