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
    String employeeAccount;

    /**
     * 员工密码
     */
    String employeePassword;

    /**
     * 二次密码
     */
    String checkPassword;
}
