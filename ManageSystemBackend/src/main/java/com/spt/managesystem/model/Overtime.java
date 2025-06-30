package com.spt.managesystem.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 加班表
 * @author zhiyuan
 * @TableName overtime
 */
@TableName(value ="overtime")
@Data
public class Overtime {
    /**
     * 主键ID
     */
    @TableId(value = "overtime_id", type = IdType.AUTO)
    private Integer overtimeId;

    /**
     * 加班员工的工号
     */
    @TableField(value = "overtime_emp_id")
    private Integer overtimeEmpId;

    /**
     * 加班员工的姓名
     */
    @TableField(value = "overtime_emp_name")
    private String overtimeEmpName;

    /**
     * 加班员工所属的部门名称
     */
    @TableField(value = "overtime_deptName")
    private String overtimeDeptName;

    /**
     * 申请的加班时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "overtime_date")
    private Date overtimeDate;

    /**
     * 加班理由
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 是否审批 0-未审批 1-已经审批
     */
    @TableField(value = "is_approve")
    private Integer isApprove;

    /**
     * 是否通过 0-未通过 1-通过
     */
    @TableField(value = "is_pass")
    private Integer isPass;

    /**
     * 创建加班申请表的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 审批完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除 0-未删除 1-已经删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 不通过的原因
     */
    @TableField(exist = false)
    private String reasonNotPass;

}