package com.buleng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.buleng.constants.SystemConstants;
import com.buleng.domain.entity.Article;
import com.buleng.domain.entity.Category;
import com.buleng.domain.entity.ResponseResult;
import com.buleng.domain.vo.ArticleDetailVo;
import com.buleng.domain.vo.ArticleListVo;
import com.buleng.domain.vo.HotArticleVo;
import com.buleng.domain.vo.PageVo;
import com.buleng.mapper.ArticleMapper;
import com.buleng.service.ArticleService;
import com.buleng.service.CategoryService;
import com.buleng.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询十条
        Page<Article> page = new Page(SystemConstants.CURRENT_PAGE, SystemConstants.SIZE);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();

        //Bean拷贝
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            articleVos.add(vo);
//        }
        List<HotArticleVo> vos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(vos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId，就要查询要和传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        //状态是正式发布的
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行排序
        queryWrapper.orderByDesc(Article::getIsTop);
        //queryWrapper.eq(Article::getIsTop, SystemConstants.ARTICLE_STATUS_DRAFT);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();
        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        //封装Vo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //获取文章id
        Article article = getById(id);
        //封装vo
        ArticleDetailVo detailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //通过id获取分类名称
        Long categoryId = article.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category!=null){
            detailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(detailVo);
    }
}
