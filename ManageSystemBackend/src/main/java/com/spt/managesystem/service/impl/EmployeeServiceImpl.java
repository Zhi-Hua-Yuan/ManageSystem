package com.spt.managesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.common.ErrorCode;
import com.spt.managesystem.common.ResultUtils;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.mapper.EmployeeMapper;
import com.spt.managesystem.model.Employee;
import com.spt.managesystem.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.spt.managesystem.common.ErrorCode.PARAMS_ERROR;
import static com.spt.managesystem.constant.EmployeeConstant.*;

/**
 * @author zhiyuan
 * @description 针对表【employee(员工表)】的数据库操作Service实现
 * @createDate 2025-06-16 19:34:13
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    /**
     * 员工登录
     */
    @Override
    public Employee employeeLogin(Integer employeeId, String account, String password, HttpServletRequest request) {
        // 1.校验账号和密码
        // 账号不小于4位，密码不小于8位
        if (account.length() < 4) {
            throw new BusinessException(PARAMS_ERROR, "账号长度应不小于4位");
        }
        if (password.length() < 8) {
            throw new BusinessException(PARAMS_ERROR, "密码长度应不小于8位");
        }
        // 2. 构造查询条件：优先使用 employeeId，account 查询用户
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", employeeId);
        queryWrapper.eq("employee_account", account);

        // 3. 查询员工信息
        Employee employee = this.getOne(queryWrapper);
        if (employee == null) {
            throw new BusinessException(PARAMS_ERROR, "用户不存在");
        }
        // 4. 使用 BCrypt 校验密码是否正确
        if (!BCrypt.checkpw(password, employee.getEmployeePassword())) {
            throw new BusinessException(PARAMS_ERROR, "密码错误");
        }
        // 5. 员工脱敏
        Employee safetyEmployee = getSafetyEmployee(employee);
        // 6. 记录员工的登录态
        request.getSession().setAttribute(EMPLOYEE_LOGIN_STATE, safetyEmployee);
        return safetyEmployee;
    }

    @Override
    public int employeeLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(EMPLOYEE_LOGIN_STATE);
        return 1;
    }

    /**
     * 注册
     */
    @Override
    public long employeeRegister(String account, String password, String checkPassword) {
        // 1.校验账号和密码
        // 账号不小于4位，密码不小于8位
        if (account.length() < 4) {
            throw new BusinessException(PARAMS_ERROR, "账号长度应不小于4位");
        }
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(PARAMS_ERROR, "密码长度应不小于8位");
        }
        // 密码和确认密码一致
        if (!password.equals(checkPassword)) {
            throw new BusinessException(PARAMS_ERROR, "密码不一致");
        }
        // 账号不能重复
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_account", account);
        Long l = employeeMapper.selectCount(queryWrapper);
        if (l > 0) {
            throw new BusinessException(PARAMS_ERROR, "账号已存在");
        }
        // 2.使用 BCrypt 加密
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // 3.向数据库中插入数据
        Employee employee = new Employee();
        employee.setEmployeeAccount(account);
        employee.setEmployeePassword(hashedPassword);
        boolean saveResult = this.save(employee);

        // 4.返回注册结果
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        return employee.getEmployeeId();
    }

    /**
     * 分页查询用户信息
     */
    @Override
    public List<Employee> searchEmployees(int pageNum, int pageSize, Employee employee) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        Page<Employee> employeePage = page(new Page<>(pageNum, pageSize), queryWrapper);

        List<Employee> records = employeePage.getRecords();
        return records.stream().map(this::getSafetyEmployee).collect(Collectors.toList());
    }


    /**
     * 员工信息脱敏
     */
    @Override
    public Employee getSafetyEmployee(Employee employee) {
        if (employee == null) {
            return null;
        }

        Employee safetyEmployee = new Employee();
        safetyEmployee.setEmployeeId(employee.getEmployeeId());
        safetyEmployee.setEmployeeName(employee.getEmployeeName());
        safetyEmployee.setEmployeeAccount(employee.getEmployeeAccount());
        safetyEmployee.setGender(employee.getGender());
        safetyEmployee.setPhone(employee.getPhone());
        safetyEmployee.setEmail(employee.getEmail());
        safetyEmployee.setBirthday(employee.getBirthday());
        safetyEmployee.setDepartmentId(employee.getDepartmentId());
        safetyEmployee.setJob(employee.getJob());
        safetyEmployee.setSalary(employee.getSalary());
        safetyEmployee.setEmployeeRole(employee.getEmployeeRole());
        safetyEmployee.setCreateTime(employee.getCreateTime());
        safetyEmployee.setUpdateTime(employee.getUpdateTime());

        return safetyEmployee;
    }

    /**
     * 判断当前用户是否是管理员
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        Employee employee = getLoginEmployee(request);
        return employee != null && employee.getEmployeeRole() == ADMIN_ROLE;
    }

    /**
     * 判断当前用户是否是部门经理
     */
    @Override
    public boolean isManager(HttpServletRequest request) {
        Employee employee = getLoginEmployee(request);
        return employee != null && employee.getEmployeeRole() == MANAGE_ROLE;
    }

    /**
     * 获取当前登录用户
     */
    @Override
    public Employee getLoginEmployee(HttpServletRequest request) {
        return (Employee) request.getSession().getAttribute(EMPLOYEE_LOGIN_STATE);
    }

}




