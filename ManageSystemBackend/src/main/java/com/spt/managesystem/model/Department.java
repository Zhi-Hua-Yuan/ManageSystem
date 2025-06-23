package com.spt.managesystem.model;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 部门表
 * @TableName department
 */
@TableName(value ="department")
@Data
public class Department {
    /**
     * 部门ID
     */
    @TableId(value = "department_id", type = IdType.AUTO)
    private Integer departmentId;

    /**
     * 部门名字
     */
    @TableField(value = "department_name")
    private String departmentName;

    /**
     * 是否删除 0-不删除 1-删除
     */
    @TableLogic
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 创建时间
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

    /**
     * 部门员工列表
     */
    @TableField(exist = false) // 表示该字段不在数据库中
    private List<Employee> employees;
}