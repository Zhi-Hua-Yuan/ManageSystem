package com.spt.managesystem.model.request;

import lombok.Data;

/**
 * @author songpintong
 */
@Data
public class ApproveHolidayRequest {
    /**
     * 请假ID
     */
    private Integer holidayId;

    /**
     * 是否通过 (1:通过, 0:不通过)
     */
    private Integer isPass;

    /**
     * 不通过原因（可选）
     */
    private String reasonNotPass;
}
