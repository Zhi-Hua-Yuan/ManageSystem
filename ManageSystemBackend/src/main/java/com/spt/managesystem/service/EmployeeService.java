package com.spt.managesystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.model.Employee;
import org.springframework.web.bind.annotation.RequestParam;

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
    Employee employeeLogin(String account, String password, HttpServletRequest request);

    /**
     * 登出
     */
    int employeeLogout(HttpServletRequest request);

    /**
     * 注册
     */
//    long employeeRegister(String account, String password, String checkPassword);

    /**
     * 添加新员工
     */
    int addEmployee(Employee employee, HttpServletRequest request);

    /**
     * 员工信息脱敏
     */
    Employee getSafetyEmployee(Employee employee);

    /**
     * 分页查询用户信息
     */
    Page<Employee> searchEmployees(int pageNum, int pageSize, Employee employee);

    /**
     * 判断当前用户是否是管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 判断当前用户是否是部门经理
     */
    boolean isManager(HttpServletRequest request);

    /**
     * 获取当前登录用户
     */
    Employee getLoginEmployee(HttpServletRequest request);

    /**
     * 参数是否合法
     */
    boolean isValid(int employeeId, HttpServletRequest request);
}
