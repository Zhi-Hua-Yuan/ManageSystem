package com.spt.managesystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.common.ErrorCode;
import com.spt.managesystem.common.ResultUtils;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.model.Employee;
import com.spt.managesystem.model.Holiday;
import com.spt.managesystem.model.Holiday;
import com.spt.managesystem.model.request.ApproveHolidayRequest;
import com.spt.managesystem.service.EmployeeService;
import com.spt.managesystem.service.HolidayService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author songpintong
 */
@RestController
@RequestMapping("/holiday")
public class HolidayController {

    @Resource
    private HolidayService holidayService;

    @Resource
    private EmployeeService employeeService;

    /**
     * 添加请假申请
     *
     * @param holiday
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Integer> addHoliday(@RequestBody Holiday holiday, HttpServletRequest request) {
        // 只有登录用户可以查看请假记录
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int result = holidayService.addHoliday(holiday);
        return ResultUtils.success(result);
    }

    /**
     * 获取请假申请详情
     *
     * @param holidayId
     * @return
     */
    @GetMapping("/{holidayId}")
    public BaseResponse<Holiday> getHolidayDetails(@PathVariable Integer holidayId) {
        Holiday holiday = holidayService.getById(holidayId);
        return ResultUtils.success(holiday);
    }

    /**
     * 条件分页查询自己所有的加班记录
     */
    @GetMapping("/current")
    public BaseResponse<Page<Holiday>> getCurrentHoliday(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer isApprove,
            @RequestParam(required = false) Integer isPass,
            HttpServletRequest request) throws ParseException {

        // 登录用户校验
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        Employee loginEmployee = employeeService.getLoginEmployee(request);

        // 构建查询条件
        QueryWrapper<Holiday> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("holiday_emp_id", loginEmployee.getEmployeeId());

        // 解析日期范围
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateTime = null;
        Date endDateTime = null;

        if (startDate != null && !startDate.isEmpty()) {
            startDateTime = sdf.parse(startDate);
            queryWrapper.ge("holiday_date", startDateTime); // 大于等于开始日期
        }

        if (endDate != null && !endDate.isEmpty()) {
            endDateTime = sdf.parse(endDate);
            queryWrapper.le("holiday_date", endDateTime); // 小于等于结束日期
        }

        // 是否审批
        if (isApprove != null) {
            queryWrapper.eq("is_approve", isApprove);
        }

        // 是否通过
        if (isPass != null) {
            queryWrapper.eq("is_pass", isPass);
        }

        // 分页查询
        Page<Holiday> page = new Page<>(pageNum, pageSize);
        Page<Holiday> resultPage = holidayService.page(page, queryWrapper);

        return ResultUtils.success(resultPage);
    }


    /**
     * 条件分页查询请假申请列表
     *
     * @param pageNum          当前页码
     * @param pageSize         每页大小
     * @param holidayEmpId    请假员工ID（可选）
     * @param holidayEmpName  请假员工姓名（可选）
     * @param holidayDeptName 部门名称（可选）
     * @param holidayDate     请假日期（可选）
     * @param isPass           是否通过（0:未通过, 1:通过）（可选）
     * @param createTime       创建时间（可选）
     * @param updateTime       更新时间（可选）
     * @param request          HTTP请求对象
     * @return 分页后的请假记录
     */
    @GetMapping("/search")
    public BaseResponse<Page<Holiday>> searchHolidayApplications(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) Integer holidayEmpId,
            @RequestParam(required = false) String holidayEmpName,
            @RequestParam(required = false) String holidayDeptName,
            @RequestParam(required = false) Date holidayDate,
            @RequestParam(required = false) Integer isPass,
            @RequestParam(required = false) Integer isApprove,
            @RequestParam(required = false) Date createTime,
            @RequestParam(required = false) Date updateTime,
            HttpServletRequest request) {

        // 只有登录用户可以查看请假记录
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        QueryWrapper<Holiday> queryWrapper = new QueryWrapper<>();

        if (holidayEmpId != null && holidayEmpId > 0) {
            queryWrapper.eq("holiday_emp_id", holidayEmpId);
        }

        if (holidayEmpName != null && !holidayEmpName.isEmpty()) {
            queryWrapper.like("holiday_emp_name", holidayEmpName);
        }

        if (holidayDeptName != null && !holidayDeptName.isEmpty()) {
            queryWrapper.like("holiday_deptName", holidayDeptName);
        }

        if (holidayDate != null) {
            queryWrapper.eq("holiday_date", holidayDate);
        }

        if (isPass != null) {
            queryWrapper.eq("is_pass", isPass);
        }

        if (isApprove != null) {
            queryWrapper.eq("is_approve", isApprove);
        }

        if (createTime != null) {
            queryWrapper.ge("create_time", createTime);
        }

        if (updateTime != null) {
            queryWrapper.ge("update_time", updateTime);
        }

        Page<Holiday> page = new Page<>(pageNum, pageSize);
        Page<Holiday> resultPage = holidayService.page(page, queryWrapper);

        return ResultUtils.success(resultPage);
    }

    /**
     * 审批请假申请
     *
     * @param requestDTO         审批请求数据
     * @param httpServletRequest HTTP请求对象
     * @return 操作结果
     */
    @PutMapping("/approve")
    public BaseResponse<Boolean> approveHoliday(@RequestBody ApproveHolidayRequest requestDTO,
                                                 HttpServletRequest httpServletRequest) {
        // 权限校验：仅系统管理员和部门经理可以审批
        Employee loginEmployee = employeeService.getLoginEmployee(httpServletRequest);
        if (loginEmployee == null || !(loginEmployee.getEmployeeRole() == 0 || loginEmployee.getEmployeeRole() == 1)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限审批");
        }

        // 执行审批
        Boolean result = holidayService.approveHoliday(
                requestDTO.getHolidayId(),
                requestDTO.getIsPass(),
                requestDTO.getReasonNotPass()
        );

        return ResultUtils.success(result);
    }

    /**
     * 删除请假申请
     *
     * @param holidayId 请假申请ID
     * @param request    HTTP请求对象
     * @return 操作结果
     */
    @PostMapping("/delete/{holidayId}")
    public BaseResponse<Boolean> deleteHoliday(@PathVariable Integer holidayId, HttpServletRequest request) {
        // 登录用户校验
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // 权限校验：仅系统管理员和部门经理可以删除请假申请
        Employee loginEmployee = employeeService.getLoginEmployee(request);
        if (loginEmployee == null || !(loginEmployee.getEmployeeRole() == 0 || loginEmployee.getEmployeeRole() == 1)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限删除请假申请");
        }

        Boolean result = holidayService.removeById(holidayId);
        return ResultUtils.success(result);
    }

}












