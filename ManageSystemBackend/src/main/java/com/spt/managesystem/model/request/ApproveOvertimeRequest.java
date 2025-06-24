package com.spt.managesystem.model.request;

import lombok.Data;

/**
 * 审批加班请求体
 * @author songpintong
 */
@Data
public class ApproveOvertimeRequest {
    /**
     * 加班ID
     */
    private Integer overtimeId;

    /**
     * 是否通过 (1:通过, 0:不通过)
     */
    private Integer isPass;

    /**
     * 不通过原因（可选）
     */
    private String reasonNotPass;
}
