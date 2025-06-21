package com.spt.managesystem.model;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
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
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}