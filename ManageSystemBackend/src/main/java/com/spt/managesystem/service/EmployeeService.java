package com.spt.managesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.model.Employee;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhiyuan
 * @description 针对表【employee(员工表)】的数据库操作Service
 * @createDate 2025-06-16 19:34:13
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 登录
     */
    BaseResponse<Employee> employeeLogin(Integer employeeId, String account, String password, HttpServletRequest request);

    /**
     * 注册
     */
    BaseResponse<Integer> employeeRegister(String account, String password, String checkPassword);

    /**
     * 员工信息脱敏
     */
    Employee getSafetyEmployee(Employee employee);

    /**
     * 更新员工信息
     */
    BaseResponse<Integer> updateEmployee(Employee newEmployee, Employee oldEmployee);

    /**
     * 分页查询用户信息
     */
    BaseResponse<List<Employee>> searchEmployees(int pageNum, int pageSize, Employee employee);

    /**
     * 判断当前用户是否是管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 判断当前用户是否是部门经理
     */
    boolean isManager(HttpServletRequest request);

}
