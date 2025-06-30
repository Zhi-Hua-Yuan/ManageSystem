package com.spt.managesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.model.Department;
import com.spt.managesystem.model.Employee;
import com.spt.managesystem.model.Overtime;
import com.spt.managesystem.service.DepartmentService;
import com.spt.managesystem.service.OvertimeService;
import com.spt.managesystem.mapper.OvertimeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.spt.managesystem.common.ErrorCode.NOT_FOUND;
import static com.spt.managesystem.common.ErrorCode.SYSTEM_ERROR;

/**
* @author zhiyuan
* @description 针对表【overtime(加班表)】的数据库操作Service实现
* @createDate 2025-06-24 08:48:02
*/
@Service
public class OvertimeServiceImpl extends ServiceImpl<OvertimeMapper, Overtime>
    implements OvertimeService{

    @Resource
    private DepartmentService departmentService;

    /**
     * 添加加班记录
     * @param overtime
     * @return
     */
    @Override
    public Integer addOvertime(Overtime overtime, Employee loginEmployee) {
        // 保存该员工的工号，姓名，所属部门
        overtime.setOvertimeEmpId(loginEmployee.getEmployeeId());
        overtime.setOvertimeEmpName(loginEmployee.getEmployeeName());
        // 获取员工的部门ID
        Integer departmentId = loginEmployee.getDepartmentId();
        // 通过部门ID查询部门名称
        Department department = departmentService.getDepartmentDetails(departmentId);
        if (department != null) {
            overtime.setOvertimeDeptName(department.getDepartmentName());
        } else {
            throw new BusinessException(SYSTEM_ERROR, "无法获取部门名称");
        }
        boolean saveResult = save(overtime);
        if (!saveResult){
            throw new BusinessException(SYSTEM_ERROR, "数据插入失败");
        }
        return overtime.getOvertimeId();
    }

    @Override
    public boolean approveOvertime(Integer overtimeId, Integer isPass, String reasonNotPass) {
        Overtime overtime = getById(overtimeId);
        if (overtime == null) {
            throw new BusinessException(NOT_FOUND, "加班记录不存在");
        }

        // 设置审批状态
        // 已审批
        overtime.setIsApprove(1);
        overtime.setIsPass(isPass);

        // 如果不通过，设置拒绝理由
        if (isPass == 0 && reasonNotPass != null && !reasonNotPass.isEmpty()) {
            // 可以复用原reason字段或新建字段
            overtime.setReason(reasonNotPass);
        }

        // 更新时间为当前时间（MyBatis Plus 自动处理 updateTime）
        return updateById(overtime);
    }
}




