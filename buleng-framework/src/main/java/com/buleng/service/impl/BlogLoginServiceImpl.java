package com.buleng.service.impl;

import com.buleng.domain.entity.LoginUser;
import com.buleng.domain.entity.ResponseResult;
import com.buleng.domain.entity.User;
import com.buleng.domain.vo.BlogUserLoginVo;
import com.buleng.domain.vo.UserInfoVo;
import com.buleng.service.BlogLoginService;
import com.buleng.utils.BeanCopyUtils;
import com.buleng.utils.JwtUtil;
import com.buleng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService{
    @Autowired//加上required=false为了解决报错
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户认证不通过(账号或者密码错误)");
        }
        //获取userId，生成token(jwt)
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
//        LoginUser loginUser = (LoginUser)userDetailsService.loadUserByUsername(user.getUserName());
        String userId = loginUser.getUser().getId().toString();

        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis中
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
        //把token和userInfo封装 返回
        //把User转化为UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo userLoginVo = new BlogUserLoginVo(jwt,userInfoVo);//@AllArgsConstructor生成了有参构造
        //这里传入userLoginVo是因为返回的data格式是
        //        {
        //            "code": 200,
        //                "data": {
        //            "token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0ODBmOThmYmJkNmI0NjM0OWUyZjY2NTM0NGNjZWY2NSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY0Mzg3NDMxNiwiZXhwIjoxNjQzOTYwNzE2fQ.ldLBUvNIxQCGemkCoMgT_0YsjsWndTg5tqfJb77pabk",
        //                    "userInfo": {
        //                "avatar": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F3bf9c263bc0f2ac5c3a7feb9e218d07475573ec8.gi",
        //                        "email": "23412332@qq.com",
        //                        "id": 1,
        //                        "nickName": "sg333",
        //                        "sex": "1"
        //            }
        //        },
        //            "msg": "操作成功"
        //        }
        return ResponseResult.okResult(userLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //通过SecurityContextHolder拿到LoginUser然后拿到userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);

        return ResponseResult.okResult();
    }
}
