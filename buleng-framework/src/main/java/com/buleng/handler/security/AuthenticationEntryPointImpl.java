package com.buleng.handler.security;

import com.alibaba.fastjson.JSON;
import com.buleng.domain.entity.ResponseResult;
import com.buleng.enums.AppHttpCodeEnum;
import com.buleng.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//        打印异常信息
//        e.printStackTrace();
//        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
//InsufficientAuthenticationException 未携带token，说明未登录
//BadCredentialsException 用户名密码错误
//InternalAuthenticationServiceException 用户名不存在
        ResponseResult result = null;
        if (e instanceof InternalAuthenticationServiceException) {
            //InternalAuthenticationServiceException 用户名不存在
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR, e.getMessage());
        } else if (e instanceof BadCredentialsException) {
            //BadCredentialsException 用户名密码错误
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR, e.getMessage());
        } else if (e instanceof InsufficientAuthenticationException) {
            //token不存在的时候提示这个，非法的时候在JwtAuthenticationTokenFilter中提示
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        } else {
            //其他异常出现
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "非认证授权异常出现！");
        }

        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
