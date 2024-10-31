package com.buleng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// @AllArgsConstructor相当于写了
// public BlogUserLoginVo(String token, UserInfoVo userInfo) {
//        this.token = token;
//        this.userInfo = userInfo;
//  }
public class BlogUserLoginVo {

    private String token;
    private UserInfoVo userInfo;

}