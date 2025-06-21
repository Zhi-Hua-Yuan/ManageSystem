package com.spt.managesystem.controller;

import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.model.Employee;
import com.spt.managesystem.model.request.EmployeeLoginRequest;
import com.spt.managesystem.model.request.EmployeeRegisterRequest;
import com.spt.managesystem.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

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
            return BaseResponse.error(-1, "请求数据为空");
        }
        Integer employeeId = employeeLoginRequest.getEmployeeId();
        String account = employeeLoginRequest.getEmployeeAccount();
        String password = employeeLoginRequest.getEmployeePassword();
        if (employeeId == null || StringUtils.isAnyBlank(account, password)) {
            return BaseResponse.error(-2, "工号、账号、密码不能为空");
        }
        if (employeeId <= 0) {
            return BaseResponse.error(-3, "工号必须大于0");
        }
        return employeeService.employeeLogin(employeeId, account, password, request);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> employeeLogout(HttpServletRequest request) {
        if (request == null){
            return BaseResponse.error(-1, "未登录");
        }
        // 移除用户登录态
        request.getSession().removeAttribute(EMPLOYEE_LOGIN_STATE);
        return BaseResponse.success(1);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public BaseResponse<Integer> employeeRegister(@RequestBody EmployeeRegisterRequest employeeRegisterRequest, HttpServletRequest request) {
        if (employeeRegisterRequest == null) {
            return BaseResponse.error(-1, "请求数据为空");
        }
//        todo 鉴权:只有系统管理员和部门经理可以注册新员工
//        if (!employeeService.isAdmin(request) && !employeeService.isManager(request)) {
//            return BaseResponse.error(-403, "权限不足");
//        }
        String account = employeeRegisterRequest.getAccount();
        String password = employeeRegisterRequest.getPassword();
        String checkPassword = employeeRegisterRequest.getCheckPassword();
        // 非空验证
        if (StringUtils.isAnyBlank(account, password, checkPassword)) {
            return BaseResponse.error(-2, "账号、密码或确认密码不能为空");
        }
        return employeeService.employeeRegister(account, password, checkPassword);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public BaseResponse<Employee> employeeInfo(HttpServletRequest request) {
        Employee employee = (Employee) request.getSession().getAttribute(EMPLOYEE_LOGIN_STATE);
        if (employee == null) {
            return BaseResponse.error(-1, "未登录");
        }
        return BaseResponse.success(employeeService.getSafetyEmployee(employee));
    }

    /**
     * 分页查询用户信息
     */
    @GetMapping("/search")
    public BaseResponse<List<Employee>> searchEmployees(int pageNum, int pageSize, HttpServletRequest request) {
        Employee employee = (Employee) request.getSession().getAttribute(EMPLOYEE_LOGIN_STATE);
        if (employee == null) {
            return BaseResponse.error(-1, "未登录");
        }
        return employeeService.searchEmployees(pageNum, pageSize,employee);
    }

    /**
     * 根据id查询用户信息
     */
    @GetMapping("/search/{id}")
    public BaseResponse<Employee> searchById(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return BaseResponse.error(-1, "请求数据为空");
        }
        // todo 写完这里
        return BaseResponse.success(employeeService.getById(id));
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/update")
    public BaseResponse<Integer> update(@RequestBody Employee newEmployee, HttpServletRequest request) {
        if (newEmployee == null || newEmployee.getEmployeeId() <= 0) {
            return BaseResponse.error(-1, "请求数据为空");
        }
        Employee oldEmployee = (Employee) request.getSession().getAttribute(EMPLOYEE_LOGIN_STATE);
        return employeeService.updateEmployee(newEmployee, oldEmployee);
    }
}
