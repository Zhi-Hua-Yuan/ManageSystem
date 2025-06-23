package com.spt.managesystem.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工表
 * @author songpintong
 * @TableName employee
 */
@TableName(value ="employee")
@Data
public class Employee {
    /**
     * 员工工号
     */
    @TableId(value = "employee_id", type = IdType.AUTO)
    private Integer employeeId;

    /**
     * 员工姓名
     */
    @TableField(value = "employee_name")
    private String employeeName;

    /**
     * 员工账号
     */
    @TableField(value = "employee_account")
    private String employeeAccount;

    /**
     * 员工密码
     */
    @TableField(value = "employee_password")
    private String employeePassword;

    /**
     * 员工性别 0-男 1-女
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 员工电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 员工邮箱号
     */
    @TableField(value = "email")
    private String email;

    /**
     * 员工出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "birthday")
    private Date birthday;

    /**
     * 所属部门的id号
     */
    @TableField(value = "department_id")
    private Integer departmentId;

    /**
     * 员工职位
     */
    @TableField(value = "job")
    private String job;

    /**
     * 工资
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "salary")
    private BigDecimal salary;

    /**
     * 用户权限 0-系统管理员 1-部门经理 2-普通员工
     */
    @TableField(value = "employee_role")
    private Integer employeeRole;

    /**
     * 是否删除 0-不删除 1-删除
     */
    @TableLogic
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 入职时间(创建时间)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "update_time")
    private Date updateTime;
}