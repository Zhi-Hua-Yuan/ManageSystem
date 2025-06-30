package com.spt.managesystem.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 出差表
 * @TableName trip
 */
@TableName(value ="trip")
@Data
public class Trip {
    /**
     * 主键ID
     */
    @TableId(value = "trip_id", type = IdType.AUTO)
    private Integer tripId;

    /**
     * 出差
     */
    @TableField(value = "trip_emp_id")
    private Integer tripEmpId;

    /**
     * 出差
     */
    @TableField(value = "trip_emp_name")
    private String tripEmpName;

    /**
     * 出差员工所属的部门名称
     */
    @TableField(value = "trip_deptName")
    private String tripDeptName;

    /**
     * 申请的出差时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(value = "trip_date")
    private Date tripDate;

    /**
     * 出差
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
     * 创建出差申请表的时间
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