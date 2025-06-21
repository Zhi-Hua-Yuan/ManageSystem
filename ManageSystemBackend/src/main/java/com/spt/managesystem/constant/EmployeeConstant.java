package com.spt.managesystem.constant;

/**
 * @author songpintong
 */
public interface EmployeeConstant {

    /**
     * 员工登录态键
     */
    String EMPLOYEE_LOGIN_STATE = "employeeLoginState";

    //  ------- 权限 --------

    /**
     * 默认权限 - 普通员工
     */
    int DEFAULT_ROLE = 2;

    /**
     * 部门经理权限
     */
    int MANAGE_ROLE = 1;

    /**
     * 系统管理员权限
     */
    int ADMIN_ROLE = 0;
}
