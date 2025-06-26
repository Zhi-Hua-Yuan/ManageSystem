package com.spt.managesystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.common.ErrorCode;
import com.spt.managesystem.common.ResultUtils;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.model.Employee;
import com.spt.managesystem.model.Trip;
import com.spt.managesystem.model.request.ApproveTripRequest;
import com.spt.managesystem.service.EmployeeService;
import com.spt.managesystem.service.TripService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author songpintong
 */
@RestController
@RequestMapping("/trip")
public class TripController {

    @Resource
    private TripService tripService;

    @Resource
    private EmployeeService employeeService;

    /**
     * 添加出差申请
     *
     * @param trip
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Integer> addTrip(@RequestBody Trip trip, HttpServletRequest request) {
        // 只有登录用户可以查看出差记录
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int result = tripService.addTrip(trip);
        return ResultUtils.success(result);
    }

    /**
     * 获取出差申请详情
     *
     * @param tripId
     * @return
     */
    @GetMapping("/details/{tripId}")
    public BaseResponse<Trip> getTripDetails(@PathVariable Integer tripId) {
        Trip trip = tripService.getById(tripId);
        return ResultUtils.success(trip);
    }

    /**
     * 条件分页查询出差申请列表
     *
     * @param pageNum          当前页码
     * @param pageSize         每页大小
     * @param tripEmpId    出差员工ID（可选）
     * @param tripEmpName  出差员工姓名（可选）
     * @param tripDeptName 部门名称（可选）
     * @param tripDate     出差日期（可选）
     * @param isPass           是否通过（0:未通过, 1:通过）（可选）
     * @param createTime       创建时间（可选）
     * @param updateTime       更新时间（可选）
     * @param request          HTTP请求对象
     * @return 分页后的出差记录
     */
    @GetMapping("/search")
    public BaseResponse<Page<Trip>> searchTripApplications(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) Integer tripEmpId,
            @RequestParam(required = false) String tripEmpName,
            @RequestParam(required = false) String tripDeptName,
            @RequestParam(required = false) Date tripDate,
            @RequestParam(required = false) Integer isPass,
            @RequestParam(required = false) Date createTime,
            @RequestParam(required = false) Date updateTime,
            HttpServletRequest request) {

        // 只有登录用户可以查看出差记录
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        QueryWrapper<Trip> queryWrapper = new QueryWrapper<>();

        if (tripEmpId != null && tripEmpId > 0) {
            queryWrapper.eq("trip_emp_id", tripEmpId);
        }

        if (tripEmpName != null && !tripEmpName.isEmpty()) {
            queryWrapper.like("trip_emp_name", tripEmpName);
        }

        if (tripDeptName != null && !tripDeptName.isEmpty()) {
            queryWrapper.like("trip_deptName", tripDeptName);
        }

        if (tripDate != null) {
            queryWrapper.eq("trip_date", tripDate);
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

        Page<Trip> page = new Page<>(pageNum, pageSize);
        Page<Trip> resultPage = tripService.page(page, queryWrapper);

        return ResultUtils.success(resultPage);
    }

    /**
     * 审批出差申请
     *
     * @param requestDTO         审批请求数据
     * @param httpServletRequest HTTP请求对象
     * @return 操作结果
     */
    @PutMapping("/approve")
    public BaseResponse<Boolean> approveTrip(@RequestBody ApproveTripRequest requestDTO,
                                                 HttpServletRequest httpServletRequest) {
        // 权限校验：仅系统管理员和部门经理可以审批
        Employee loginEmployee = employeeService.getLoginEmployee(httpServletRequest);
        if (loginEmployee == null || !(loginEmployee.getEmployeeRole() == 0 || loginEmployee.getEmployeeRole() == 1)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限审批");
        }

        // 执行审批
        Boolean result = tripService.approveTrip(
                requestDTO.getTripId(),
                requestDTO.getIsPass(),
                requestDTO.getReasonNotPass()
        );

        return ResultUtils.success(result);
    }

    /**
     * 删除出差申请
     *
     * @param tripId 出差申请ID
     * @param request    HTTP请求对象
     * @return 操作结果
     */
    @PostMapping("/delete/{tripId}")
    public BaseResponse<Boolean> deleteTrip(@PathVariable Integer tripId, HttpServletRequest request) {
        // 登录用户校验
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // 权限校验：仅系统管理员和部门经理可以删除出差申请
        Employee loginEmployee = employeeService.getLoginEmployee(request);
        if (loginEmployee == null || !(loginEmployee.getEmployeeRole() == 0 || loginEmployee.getEmployeeRole() == 1)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限删除出差申请");
        }

        Boolean result = tripService.removeById(tripId);
        return ResultUtils.success(result);
    }

}












