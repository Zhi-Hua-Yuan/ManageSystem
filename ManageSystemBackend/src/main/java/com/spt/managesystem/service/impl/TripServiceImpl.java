package com.spt.managesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.model.Trip;
import com.spt.managesystem.service.TripService;
import com.spt.managesystem.mapper.TripMapper;
import org.springframework.stereotype.Service;

import static com.spt.managesystem.common.ErrorCode.NOT_FOUND;
import static com.spt.managesystem.common.ErrorCode.SYSTEM_ERROR;

/**
 * @author zhiyuan
 * @description 针对表【trip(出差表)】的数据库操作Service实现
 * @createDate 2025-06-24 08:48:02
 */
@Service
public class TripServiceImpl extends ServiceImpl<TripMapper, Trip>
        implements TripService{

    /**
     * 添加出差记录
     * @param trip
     * @return
     */
    @Override
    public Integer addTrip(Trip trip) {
        boolean saveResult = save(trip);
        if (!saveResult){
            throw new BusinessException(SYSTEM_ERROR, "数据插入失败");
        }
        return trip.getTripId();
    }

    @Override
    public boolean approveTrip(Integer tripId, Integer isPass, String reasonNotPass) {
        Trip trip = getById(tripId);
        if (trip == null) {
            throw new BusinessException(NOT_FOUND, "出差记录不存在");
        }

        // 设置审批状态
        // 已审批
        trip.setIsApprove(1);
        trip.setIsPass(isPass);

        // 如果不通过，设置拒绝理由
        if (isPass == 0 && reasonNotPass != null && !reasonNotPass.isEmpty()) {
            // 可以复用原reason字段或新建字段
            trip.setReason(reasonNotPass);
        }

        // 更新时间为当前时间（MyBatis Plus 自动处理 updateTime）
        return updateById(trip);
    }
}




