package com.spt.managesystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.common.ErrorCode;
import com.spt.managesystem.common.ResultUtils;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.model.Employee;
import com.spt.managesystem.model.request.EmployeeLoginRequest;
import com.spt.managesystem.model.request.EmployeeRegisterRequest;
import com.spt.managesystem.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.spt.managesystem.common.ErrorCode.*;
import static com.spt.managesystem.constant.EmployeeConstant.DEFAULT_ROLE;
import static com.spt.managesystem.constant.EmployeeConstant.EMPLOYEE_LOGIN_STATE;

/**
 * @author songpintong
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public BaseResponse<Employee> employeeLogin(@RequestBody EmployeeLoginRequest employeeLoginRequest, HttpServletRequest request) {
        if (employeeLoginRequest == null) {
            throw new BusinessException(PARAMS_ERROR);
        }
        String account = employeeLoginRequest.getEmployeeAccount();
        String password = employeeLoginRequest.getEmployeePassword();
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BusinessException(NULL_ERROR);
        }

        Employee employee = employeeService.employeeLogin(account, password, request);
        return ResultUtils.success(employee);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> employeeLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(NOT_LOGIN);
        }
        int result = employeeService.employeeLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 注册
     */
//    @PostMapping("/register")
//    public BaseResponse<Long> employeeRegister(@RequestBody EmployeeRegisterRequest employeeRegisterRequest, HttpServletRequest request) {
//        if (employeeRegisterRequest == null) {
//            throw new BusinessException(PARAMS_ERROR);
//        }
//        // 鉴权: 只有系统管理员和部门经理可以注册新员工
//        if (!(employeeService.isAdmin(request) || employeeService.isManager(request))) {
//            throw new BusinessException(ErrorCode.NO_AUTH, "只有系统管理员和部门经理可以注册新员工");
//        }
//        String account = employeeRegisterRequest.getEmployeeAccount();
//        String password = employeeRegisterRequest.getEmployeePassword();
//        String checkPassword = employeeRegisterRequest.getCheckPassword();
//        // 非空验证
//        if (StringUtils.isAnyBlank(account, password, checkPassword)) {
//            throw new BusinessException(NULL_ERROR);
//        }
//        long result = employeeService.employeeRegister(account, password, checkPassword);
//        return ResultUtils.success(result);
//    }

    /**
     * 添加新员工
     */
    @PostMapping("/add")
    public BaseResponse<Long> addEmployee(@RequestBody Employee employee, HttpServletRequest request) {
        if (!employeeService.isAdmin(request)) {
            throw new BusinessException(NO_AUTH);
        }
        long result = employeeService.addEmployee(employee,request);
        return ResultUtils.success(result);
    }

    /**
     * 获取自己的详细信息
     */
    @GetMapping("/current")
    public BaseResponse<Employee> employeeInfo(HttpServletRequest request) {
        Employee employee = employeeService.getLoginEmployee(request);
        if (employee == null) {
            throw new BusinessException(NOT_LOGIN);
        }
        Employee safetyEmployee = employeeService.getSafetyEmployee(employee);
        return ResultUtils.success(safetyEmployee);
    }

    /**
     * 获取其他员工信息
     */
    @GetMapping("/get/{employeeId}")
    public BaseResponse<Employee> getEmployeeById(@PathVariable Integer employeeId, HttpServletRequest request) {
        // 只有系统管理员和部门经理可以查询其他员工信息
        if (!(employeeService.isAdmin(request) || employeeService.isManager(request))) {
            throw new BusinessException(NO_AUTH);
        }
        Employee employee = employeeService.getById(employeeId);
        if (employee == null) {
            throw new BusinessException(NULL_ERROR, "用户不存在");
        }
        Employee safetyEmployee = employeeService.getSafetyEmployee(employee);
        return ResultUtils.success(safetyEmployee);
    }


    /**
     * 分页查询用户信息
     */
    @GetMapping("/search")
    public BaseResponse<Page<Employee>> searchEmployees(Integer pageNum, Integer pageSize, HttpServletRequest request) {
        Employee employee = employeeService.getLoginEmployee(request);
        if (employee == null) {
            throw new BusinessException(NOT_LOGIN);
        }
        // 普通员工不能查询
        if (employee.getEmployeeRole() == DEFAULT_ROLE) {
            throw new BusinessException(NO_AUTH);
        }
        Page<Employee> page = employeeService.searchEmployees(pageNum, pageSize, employee);
        return ResultUtils.success(page);
    }

    /**
     * 条件查询用户信息（支持按工号、姓名、部门查询）
     */
    @GetMapping("/search/tags")
    public BaseResponse<List<Employee>> searchEmployeesByCondition(
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) Integer departmentId,
            HttpServletRequest request) {
        Employee loginEmployee = employeeService.getLoginEmployee(request);
        if (loginEmployee == null) {
            throw new BusinessException(NOT_LOGIN);
        }
        // 普通员工不能查询
        if (loginEmployee.getEmployeeRole() == DEFAULT_ROLE) {
            throw new BusinessException(NO_AUTH);
        }

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        if (employeeId != null && employeeId > 0) {
            wrapper.eq(Employee::getEmployeeId, employeeId);
        }
        if (StringUtils.isNotBlank(employeeName)) {
            wrapper.like(Employee::getEmployeeName, employeeName);
        }
        if (departmentId != null && departmentId > 0) {
            wrapper.eq(Employee::getDepartmentId, departmentId);
        }

        List<Employee> employees = employeeService.list(wrapper);
        List<Employee> safetyEmployees = employees.stream()
                .map(employeeService::getSafetyEmployee)
                .collect(Collectors.toList());
        return ResultUtils.success(safetyEmployees);
    }

    /**
     * 修改员工信息
     */
    @PutMapping("/update")
    public BaseResponse<Boolean> updateEmployee(@RequestBody Employee newEmployee, HttpServletRequest request) {
        if (newEmployee == null || newEmployee.getEmployeeId() <= 0) {
            throw new BusinessException(PARAMS_ERROR);
        }
        if (request == null) {
            throw new BusinessException(NOT_LOGIN);
        }
        Employee oldEmployee = employeeService.getLoginEmployee(request);
        // 只有系统管理员和部门经理可以修改其他员工信息
        // 自己可以修改自己的信息
        if (!oldEmployee.getEmployeeId().equals(newEmployee.getEmployeeId())
                &&
                !(employeeService.isAdmin(request) || employeeService.isManager(request))) {
            throw new BusinessException(NO_AUTH);
        }

        boolean result = employeeService.updateById(newEmployee);
        return ResultUtils.success(result);
    }

    /**
     * 删除员工信息
     */
    @PostMapping("/delete/{employeeId}")
    public BaseResponse<Boolean> deleteEmployee(@PathVariable Integer employeeId, HttpServletRequest request) {
        if (!employeeService.isValid(employeeId, request)) {
            return ResultUtils.success(false);
        }
        // 只有系统管理员和部门经理可以删除其他员工信息
        if (!employeeService.isAdmin(request) && !employeeService.isManager(request)) {
            throw new BusinessException(NO_AUTH);
        }
        boolean result = employeeService.removeById(employeeId);
        return ResultUtils.success(result);
    }

    /**
     * 重置员工密码
     */
    @PutMapping("/resetPassword/{employeeId}")
    public BaseResponse<Boolean> resetPassword(@PathVariable Integer employeeId, HttpServletRequest request) {
        if (!employeeService.isValid(employeeId, request)) {
            return ResultUtils.success(false);
        }
        // 只有管理员和部门经理可以重置密码
        if (!employeeService.isAdmin(request) && !employeeService.isManager(request)) {
            throw new BusinessException(NO_AUTH);
        }
        Employee employee = employeeService.getById(employeeId);
        // 用户存在
        if (employee == null) {
            throw new BusinessException(NOT_FOUND);
        }
        employee.setEmployeePassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));
        boolean result = employeeService.updateById(employee);
        if (!result) {
            throw new BusinessException(SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

}
