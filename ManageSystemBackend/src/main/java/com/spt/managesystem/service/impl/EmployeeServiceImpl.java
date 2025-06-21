package com.spt.managesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.mapper.EmployeeMapper;
import com.spt.managesystem.model.Employee;
import com.spt.managesystem.service.EmployeeService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

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
    public BaseResponse<Employee> employeeLogin(Integer employeeId, String account, String password, HttpServletRequest request) {
        // 1. 构造查询条件：优先使用 employeeId，account 查询用户
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", employeeId);
        queryWrapper.eq("employee_account", account);

        // 2. 查询员工信息
        Employee employee = this.getOne(queryWrapper);
        if (employee == null) {
            return BaseResponse.error(-1, "用户不存在");
        }
        // 3. 使用 BCrypt 校验密码是否正确
        if (!BCrypt.checkpw(password, employee.getEmployeePassword())) {
            return BaseResponse.error(-2, "密码错误");
        }

        // 4. 员工脱敏
        Employee safetyEmployee = getSafetyEmployee(employee);
        // 5. 记录员工的登录态
        request.getSession().setAttribute(EMPLOYEE_LOGIN_STATE, safetyEmployee);
        return BaseResponse.success(safetyEmployee);
    }

    /**
     * 注册
     */
    @Override
    public BaseResponse<Integer> employeeRegister(String account, String password, String checkPassword) {
        // 1.校验账号和密码
        // 账号不小于4位，密码不小于8位
        if (account.length() < 4) {
            return BaseResponse.error(-3, "账号长度应不小于4位");
        }
        if (password.length() < 8) {
            return BaseResponse.error(-4, "密码长度应不小于8位");

        }
        // 密码和确认密码一致
        if (!password.equals(checkPassword)) {
            return BaseResponse.error(-5, "密码和确认密码不一致");
        }
        // 账号不能重复
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_account", account);
        Long l = employeeMapper.selectCount(queryWrapper);
        if (l > 0) {
            return BaseResponse.error(-6, "已存在相同账号");
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
            return BaseResponse.error(-7, "注册失败");
        }
        return BaseResponse.success(employee.getEmployeeId());
    }

    /**
     * 更新员工信息
     */
    @Override
    public BaseResponse<Integer> updateEmployee(Employee newEmployee, Employee oldEmployee) {
        // 鉴权
        // 如果是管理员，可以修改所有人的信息
        Integer employeeRole = oldEmployee.getEmployeeRole();
        if (employeeRole == ADMIN_ROLE) {
            // 执行更新操作
            boolean updateResult = this.updateById(newEmployee);
            return updateResult ? BaseResponse.success(1) : BaseResponse.error(-9, "更新失败");
        }
        // 如果是部门经理，可以修改自己部门员工的信息
        else if (employeeRole == MANAGE_ROLE) {
            // 只能修改同部门员工的信息
            if (newEmployee.getDepartmentId().equals(oldEmployee.getDepartmentId())) {
                // 执行更新操作
                boolean updateResult = this.updateById(newEmployee);
                return updateResult ? BaseResponse.success(1) : BaseResponse.error(-9, "更新失败");
            } else {
                return BaseResponse.error(-10, "无权限修改其他部门员工信息");
            }
        }
        // 如果是普通员工，可以修改自己的信息
        else if (employeeRole == DEFAULT_ROLE) {
            // 只能修改自己的信息
            if (newEmployee.getEmployeeId().equals(oldEmployee.getEmployeeId())) {
                // 执行更新操作
                boolean updateResult = this.updateById(newEmployee);
                return updateResult ? BaseResponse.success(1) : BaseResponse.error(-9, "更新失败");
            } else {
                return BaseResponse.error(-11, "无权限修改他人信息");
            }
        } else {
            return BaseResponse.error(-12, "没有操作权限");
        }
    }

    /**
     * 分页查询用户信息
     */
    @Override
    public BaseResponse<List<Employee>> searchEmployees(int pageNum, int pageSize, Employee employee) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        // 系统管理员可以查询所有员工信息
        // 部门经理只能查询本部门的员工信息
        // 普通员工不能查询
        if (employee.getEmployeeRole() == MANAGE_ROLE) {
            queryWrapper.eq("department_id", employee.getDepartmentId());
        } else if (employee.getEmployeeRole() == DEFAULT_ROLE) {
            return BaseResponse.error(-13, "没有操作权限");
        }
        Page<Employee> employeePage = page(new Page<>(pageNum, pageSize), queryWrapper);
        return BaseResponse.success(employeePage.getRecords());
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
        Employee employee = (Employee) request.getSession().getAttribute(EMPLOYEE_LOGIN_STATE);
        return employee != null && employee.getEmployeeRole() == 0;
    }

    /**
     * 判断当前用户是否是部门经理
     */
    @Override
    public boolean isManager(HttpServletRequest request) {
        Employee employee = (Employee) request.getSession().getAttribute(EMPLOYEE_LOGIN_STATE);
        return employee != null && employee.getEmployeeRole() == 1;
    }


}




