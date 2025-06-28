package com.spt.managesystem.model.request;

import lombok.Data;

/**
 * @author songpintong
 * 用户登录请求体
 */
@Data
public class EmployeeLoginRequest {

    /**
     * 员工账号
     */
    String employeeAccount;

    /**
     * 员工密码
     */
    String employeePassword;
}
