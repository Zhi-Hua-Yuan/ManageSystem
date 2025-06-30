package com.spt.managesystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.common.ErrorCode;
import com.spt.managesystem.common.ResultUtils;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.model.Employee;
import com.spt.managesystem.model.Overtime;
import com.spt.managesystem.model.request.ApproveOvertimeRequest;
import com.spt.managesystem.service.EmployeeService;
import com.spt.managesystem.service.OvertimeService;
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
@RequestMapping("/overtime")
public class OvertimeController {

    @Resource
    private OvertimeService overtimeService;

    @Resource
    private EmployeeService employeeService;

    /**
     * 添加加班申请
     *
     * @param overtime
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Integer> addOvertime(@RequestBody Overtime overtime, HttpServletRequest request) {
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Employee loginEmployee = employeeService.getLoginEmployee(request);
        int result = overtimeService.addOvertime(overtime,loginEmployee);
        return ResultUtils.success(result);
    }

    /**
     * 获取加班申请详情
     *
     * @param overtimeId
     * @return
     */
    @GetMapping("/{overtimeId}")
    public BaseResponse<Overtime> getOvertimeDetails(@PathVariable Integer overtimeId) {
        Overtime overtime = overtimeService.getById(overtimeId);
        return ResultUtils.success(overtime);
    }

    /**
     * 条件分页查询自己所有的加班记录
     */
    @GetMapping("/current")
    public BaseResponse<Page<Overtime>> getCurrentOvertime(
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
        QueryWrapper<Overtime> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("overtime_emp_id", loginEmployee.getEmployeeId());

        // 解析日期范围
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateTime = null;
        Date endDateTime = null;

        if (startDate != null && !startDate.isEmpty()) {
            startDateTime = sdf.parse(startDate);
            queryWrapper.ge("overtime_date", startDateTime); // 大于等于开始日期
        }

        if (endDate != null && !endDate.isEmpty()) {
            endDateTime = sdf.parse(endDate);
            queryWrapper.le("overtime_date", endDateTime); // 小于等于结束日期
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
        Page<Overtime> page = new Page<>(pageNum, pageSize);
        Page<Overtime> resultPage = overtimeService.page(page, queryWrapper);

        return ResultUtils.success(resultPage);
    }


    /**
     * 条件分页查询加班申请列表
     *
     * @param pageNum          当前页码
     * @param pageSize         每页大小
     * @param overtimeEmpId    加班员工ID（可选）
     * @param overtimeEmpName  加班员工姓名（可选）
     * @param overtimeDeptName 部门名称（可选）
     * @param overtimeDate     加班日期（可选）
     * @param isPass           是否通过（0:未通过, 1:通过）（可选）
     * @param createTime       创建时间（可选）
     * @param updateTime       更新时间（可选）
     * @param request          HTTP请求对象
     * @return 分页后的加班记录
     */
    @GetMapping("/search")
    public BaseResponse<Page<Overtime>> searchOvertime(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) Integer overtimeEmpId,
            @RequestParam(required = false) String overtimeEmpName,
            @RequestParam(required = false) String overtimeDeptName,
            @RequestParam(required = false) Date overtimeDate,
            @RequestParam(required = false) Integer isPass,
            @RequestParam(required = false) Integer isApprove,
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

        if (isApprove != null) {
            queryWrapper.eq("is_approve", isApprove);
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

    /**
     * 审批加班申请
     *
     * @param requestDTO         审批请求数据
     * @param httpServletRequest HTTP请求对象
     * @return 操作结果
     */
    @PutMapping("/approve")
    public BaseResponse<Boolean> approveOvertime(@RequestBody ApproveOvertimeRequest requestDTO,
                                                 HttpServletRequest httpServletRequest) {
        // 权限校验：仅系统管理员和部门经理可以审批
        Employee loginEmployee = employeeService.getLoginEmployee(httpServletRequest);
        if (loginEmployee == null || !(loginEmployee.getEmployeeRole() == 0 || loginEmployee.getEmployeeRole() == 1)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限审批");
        }

        // 执行审批
        Boolean result = overtimeService.approveOvertime(
                requestDTO.getOvertimeId(),
                requestDTO.getIsPass(),
                requestDTO.getReasonNotPass()
        );

        return ResultUtils.success(result);
    }

    /**
     * 删除加班申请
     *
     * @param overtimeId 加班申请ID
     * @param request    HTTP请求对象
     * @return 操作结果
     */
    @PostMapping("/delete/{overtimeId}")
    public BaseResponse<Boolean> deleteOvertime(@PathVariable Integer overtimeId, HttpServletRequest request) {
        // 登录用户校验
        if (request.getSession().getAttribute("employeeLoginState") == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // 权限校验：仅系统管理员和部门经理可以删除加班申请
        Employee loginEmployee = employeeService.getLoginEmployee(request);
        if (loginEmployee == null || !(loginEmployee.getEmployeeRole() == 0 || loginEmployee.getEmployeeRole() == 1)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限删除加班申请");
        }

        Boolean result = overtimeService.removeById(overtimeId);
        return ResultUtils.success(result);
    }

}












