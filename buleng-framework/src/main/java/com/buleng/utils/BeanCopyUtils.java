package com.buleng.utils;

import com.buleng.domain.entity.Article;
import com.buleng.domain.vo.HotArticleVo;
import lombok.val;
import org.springframework.beans.BeanUtils;

import javax.xml.transform.Result;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    /**
     * 私有构造，表示当前类不能直接被new出来
     */
    private BeanCopyUtils() {

    }

    /**
     * 拷贝一个实体类的属性值到另一个对象中，为了截取只返回部分值
     * @param source
     * @param clazz
     * @param <V>
     * @return
     */
    public static <V>V copyBean(Object source, Class<V> clazz) {
        //提升result作用域
        V result = null;
        try {
            //创建目标对象
            result = clazz.newInstance();
            //实现属性拷贝
            BeanUtils.copyProperties(source,result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 复制一个List类型的实体类到另一个对象中
     * @param list
     * @param clazz
     * @param <O>
     * @param <V>
     * @return
     */
    public static <O,V> List<V> copyBeanList(List<O> list,Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }

    /**
     * 测试复制bean工具
     * @param args
     */
    public static void main(String[] args) {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("测试标题");
        article.setUpdateBy(1L);
        article.setUpdateTime(new Date());
        article.setViewCount(0L);

        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
        System.out.println(hotArticleVo);
    }
}
