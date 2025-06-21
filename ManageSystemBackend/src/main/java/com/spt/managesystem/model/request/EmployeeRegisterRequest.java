package com.spt.managesystem.model.request;

import lombok.Data;

/**
 * @author songpintong
 * 用户注册请求体
 */
@Data
public class EmployeeRegisterRequest {

    /**
     * 员工账号
     */
    String account;

    /**
     * 员工密码
     */
    String password;

    /**
     * 二次密码
     */
    String checkPassword;
}
