package com.spt.managesystem.service;

import com.spt.managesystem.model.Trip;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author zhiyuan
 * @description 针对表【trip(出差表)】的数据库操作Service
 * @createDate 2025-06-24 08:48:02
 */
public interface TripService extends IService<Trip> {

    /**
     * 添加出差信息
     * @param trip
     * @return
     */
    Integer addTrip(Trip trip);

    /**
     * 审批出差申请
     * @param tripId 出差ID
     * @param isPass 是否通过
     * @param reasonNotPass 不通过原因
     * @return 是否成功
     */
    boolean approveTrip(Integer tripId, Integer isPass, String reasonNotPass);

}
