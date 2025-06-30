package com.spt.managesystem.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 请假表
 * @TableName holiday
 */
@TableName(value ="holiday")
@Data
public class Holiday {
    /**
     * 主键ID
     */
    @TableId(value = "holiday_id", type = IdType.AUTO)
    private Integer holidayId;

    /**
     * 请假员工的工号
     */
    @TableField(value = "holiday_emp_id")
    private Integer holidayEmpId;

    /**
     * 请假员工的姓名
     */
    @TableField(value = "holiday_emp_name")
    private String holidayEmpName;

    /**
     * 请假员工所属的部门名称
     */
    @TableField(value = "holiday_deptName")
    private String holidayDeptName;

    /**
     * 申请的请假时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "holiday_date")
    private Date holidayDate;

    /**
     * 请假理由
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
     * 创建请假申请表的时间
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