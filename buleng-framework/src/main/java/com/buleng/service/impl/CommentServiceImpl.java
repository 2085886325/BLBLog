package com.buleng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.buleng.domain.entity.Comment;
import com.buleng.domain.entity.ResponseResult;
import com.buleng.domain.vo.CommentVo;
import com.buleng.domain.vo.PageVo;
import com.buleng.enums.AllStaticValue;
import com.buleng.mapper.CommentMapper;
import com.buleng.service.CommentService;
import com.buleng.service.UserService;
import com.buleng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-04-11 16:30:07
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;


    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断，查询要和传入的相同
        queryWrapper.eq(Objects.nonNull(articleId) && articleId > 0, Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getRootId, AllStaticValue.RootId);

        Page<Comment> page = new Page(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        //将传入的comment类型的list复制给commentVo
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历commentVo，当前username和toCommentUserName为空
        for (CommentVo commentVo : commentVos) {
            //通过id获取到User对象，再调用User对象的getNickName获取昵称
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            //把获取的昵称设置到当前CommentVo
            commentVo.setUsername(nickName);
            //判断是否为根评论
            if(commentVo.getRootId()!=-1){
                //通过id获取到User对象，再调用User对象的getNickName获取昵称
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                //把获取的昵称设置到当前CommentVo的toCommentUserName
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}
