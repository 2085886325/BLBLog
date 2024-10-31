package com.buleng.handler.exception;

import com.buleng.domain.entity.ResponseResult;
import com.buleng.enums.AppHttpCodeEnum;
import com.buleng.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice = @ControllerAdvice + @ResponseBody
@RestControllerAdvice
//日志 酸辣粉四斤
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e) {
        log.error("出现了异常！ ", e);
        return ResponseResult.errorResult(e.getCode(), e.getMessage());
    }

    //对意料之外的异常做一个处理
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error("出现了异常！ {}", e);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }
}
