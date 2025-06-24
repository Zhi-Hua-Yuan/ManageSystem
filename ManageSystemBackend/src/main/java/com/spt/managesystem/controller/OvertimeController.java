package com.spt.managesystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.common.ErrorCode;
import com.spt.managesystem.common.ResultUtils;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.model.Overtime;
import com.spt.managesystem.service.OvertimeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author songpintong
 */
@RestController
@RequestMapping("/overtime")
public class OvertimeController {

    @Resource
    private OvertimeService overtimeService;

    /**
     * 添加加班申请
     * @param overtime
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Integer> addOvertime(@RequestBody Overtime overtime, HttpServletRequest  request){
        // 只有登录用户可以查看加班记录
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int result = overtimeService.addOvertime(overtime);
        return ResultUtils.success(result);
    }

    /**
     * 获取加班申请详情
     * @param overtimeId
     * @return
     */
    @GetMapping("/details/{overtimeId}")
    public BaseResponse<Overtime> getOvertimeDetails(@PathVariable Integer overtimeId){
        Overtime overtime = overtimeService.getById(overtimeId);
        return ResultUtils.success(overtime);
    }

    /**
     * 条件分页查询加班申请列表
     *
     * @param pageNum           当前页码
     * @param pageSize          每页大小
     * @param overtimeEmpId     加班员工ID（可选）
     * @param overtimeEmpName   加班员工姓名（可选）
     * @param overtimeDeptName  部门名称（可选）
     * @param overtimeDate      加班日期（可选）
     * @param isPass            是否通过（0:未通过, 1:通过）（可选）
     * @param createTime        创建时间（可选）
     * @param updateTime        更新时间（可选）
     * @param request           HTTP请求对象
     * @return 分页后的加班记录
     */
    @GetMapping("/search")
    public BaseResponse<Page<Overtime>> searchOvertimeApplications(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) Integer overtimeEmpId,
            @RequestParam(required = false) String overtimeEmpName,
            @RequestParam(required = false) String overtimeDeptName,
            @RequestParam(required = false) Date overtimeDate,
            @RequestParam(required = false) Integer isPass,
            @RequestParam(required = false) Date createTime,
            @RequestParam(required = false) Date updateTime,
            HttpServletRequest request) {

        // 只有登录用户可以查看加班记录
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        QueryWrapper<Overtime> queryWrapper = new QueryWrapper<>();

        if (overtimeEmpId != null && overtimeEmpId > 0) {
            queryWrapper.eq("overtime_emp_id", overtimeEmpId);
        }

        if (overtimeEmpName != null && !overtimeEmpName.isEmpty()) {
            queryWrapper.like("overtime_emp_name", overtimeEmpName);
        }

        if (overtimeDeptName != null && !overtimeDeptName.isEmpty()) {
            queryWrapper.like("overtime_deptName", overtimeDeptName);
        }

        if (overtimeDate != null) {
            queryWrapper.eq("overtime_date", overtimeDate);
        }

        if (isPass != null) {
            queryWrapper.eq("is_pass", isPass);
        }

        if (createTime != null) {
            queryWrapper.ge("create_time", createTime);
        }

        if (updateTime != null) {
            queryWrapper.ge("update_time", updateTime);
        }

        Page<Overtime> page = new Page<>(pageNum, pageSize);
        Page<Overtime> resultPage = overtimeService.page(page, queryWrapper);

        return ResultUtils.success(resultPage);
    }
}












