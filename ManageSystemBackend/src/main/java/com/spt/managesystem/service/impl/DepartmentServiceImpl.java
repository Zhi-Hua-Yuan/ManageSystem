package com.spt.managesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.mapper.DepartmentMapper;
import com.spt.managesystem.model.Department;
import com.spt.managesystem.model.Employee;
import com.spt.managesystem.service.DepartmentService;
import com.spt.managesystem.service.EmployeeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.spt.managesystem.common.ErrorCode.NOT_FOUND;
import static com.spt.managesystem.common.ErrorCode.SYSTEM_ERROR;

/**
 * @author zhiyuan
 * @description 针对表【department(部门表)】的数据库操作Service实现
 * @createDate 2025-06-16 19:39:01
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {

    @Resource
    private EmployeeService employeeService;


    /**
     * 添加部门
     *
     * @param departmentName 部门名称
     * @return 部门id
     */
    @Override
    public Integer addDepartment(String departmentName) {
        Department department = new Department();
        department.setDepartmentName(departmentName);
        boolean saveResult = save(department);
        if (!saveResult) {
            throw new BusinessException(SYSTEM_ERROR, "数据插入失败");
        }
        return department.getDepartmentId();
    }

    /**
     * 更新部门信息
     *
     * @param departmentId      部门ID
     * @param newDepartmentName 新的部门名称
     * @return 是否更新成功
     */
    @Override
    public boolean updateDepartment(Integer departmentId, String newDepartmentName) {
        Department department = getById(departmentId);
        if (department == null) {
            throw new BusinessException(NOT_FOUND, "部门不存在");
        }
        department.setDepartmentName(newDepartmentName);
        return updateById(department);
    }

    /**
     * 分页查询部门
     *
     * @param pageNum        当前页码
     * @param pageSize       每页大小
     * @param departmentName 部门名称（用于条件查询）
     * @return 查询结果
     */
    @Override
    public List<Department> searchDepartments(int pageNum, int pageSize, String departmentName) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(departmentName)) {
            queryWrapper.like("department_name", departmentName);
        }

        Page<Department> departmentPage = page(new Page<>(pageNum, pageSize), queryWrapper);
        return departmentPage.getRecords();
    }


    /**
     * 获取部门详情（包括部门下的员工）
     *
     * @param departmentId 部门ID
     * @return 部门详情
     */
    @Override
    public Department getDepartmentDetails(Integer departmentId) {
        Department department = getById(departmentId);
        if (department == null) {
            throw new BusinessException(NOT_FOUND, "部门不存在");
        }

        // 获取部门下的员工列表
        List<Employee> employees = employeeService.list(new QueryWrapper<Employee>()
                .eq("department_id", departmentId));

        department.setEmployees(employees);
        return department;
    }

}




