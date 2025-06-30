package com.spt.managesystem.service;

import com.spt.managesystem.model.Holiday;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spt.managesystem.model.Holiday;

/**
* @author zhiyuan
* @description 针对表【holiday(请假表)】的数据库操作Service
* @createDate 2025-06-25 16:54:12
*/
public interface HolidayService extends IService<Holiday> {

    /**
     * 添加请假信息
     * @param holiday
     * @return
     */
    Integer addHoliday(Holiday holiday);

    /**
     * 审批请假申请
     * @param holidayId 请假ID
     * @param isPass 是否通过
     * @param reasonNotPass 不通过原因
     * @return 是否成功
     */
    boolean approveHoliday(Integer holidayId, Integer isPass, String reasonNotPass);

}
