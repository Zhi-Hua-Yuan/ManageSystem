package com.spt.managesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.model.Holiday;
import com.spt.managesystem.model.Holiday;
import com.spt.managesystem.service.HolidayService;
import com.spt.managesystem.mapper.HolidayMapper;
import org.springframework.stereotype.Service;

import static com.spt.managesystem.common.ErrorCode.NOT_FOUND;
import static com.spt.managesystem.common.ErrorCode.SYSTEM_ERROR;

/**
 * @author zhiyuan
 * @description 针对表【holiday(请假表)】的数据库操作Service实现
 * @createDate 2025-06-25 16:54:12
 */
@Service
public class HolidayServiceImpl extends ServiceImpl<HolidayMapper, Holiday>
        implements HolidayService {
    /**
     * 添加请假记录
     *
     * @param holiday
     * @return
     */
    @Override
    public Integer addHoliday(Holiday holiday) {
        boolean saveResult = save(holiday);
        if (!saveResult) {
            throw new BusinessException(SYSTEM_ERROR, "数据插入失败");
        }
        return holiday.getHolidayId();
    }

    @Override
    public boolean approveHoliday(Integer holidayId, Integer isPass, String reasonNotPass) {
        Holiday holiday = getById(holidayId);
        if (holiday == null) {
            throw new BusinessException(NOT_FOUND, "请假记录不存在");
        }

        // 设置审批状态
        // 已审批
        holiday.setIsApprove(1);
        holiday.setIsPass(isPass);

        // 如果不通过，设置拒绝理由
        if (isPass == 0 && reasonNotPass != null && !reasonNotPass.isEmpty()) {
            // 可以复用原reason字段或新建字段
            holiday.setReason(reasonNotPass);
        }

        // 更新时间为当前时间（MyBatis Plus 自动处理 updateTime）
        return updateById(holiday);
    }
}




