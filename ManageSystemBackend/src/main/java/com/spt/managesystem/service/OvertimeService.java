package com.spt.managesystem.service;

import com.spt.managesystem.model.Employee;
import com.spt.managesystem.model.Overtime;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhiyuan
* @description 针对表【overtime(加班表)】的数据库操作Service
* @createDate 2025-06-24 08:48:02
*/
public interface OvertimeService extends IService<Overtime> {

    /**
     * 添加加班信息
     * @param overtime
     * @return
     */
    Integer addOvertime(Overtime overtime, Employee loginEmployee);

    /**
     * 审批加班申请
     * @param overtimeId 加班ID
     * @param isPass 是否通过
     * @param reasonNotPass 不通过原因
     * @return 是否成功
     */
    boolean approveOvertime(Integer overtimeId, Integer isPass, String reasonNotPass);

}
