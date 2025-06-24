package com.spt.managesystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spt.managesystem.model.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhiyuan
* @description 针对表【department(部门表)】的数据库操作Service
* @createDate 2025-06-16 19:39:01
*/
public interface DepartmentService extends IService<Department> {

    /**
     * 添加部门
     * @param departmentName 部门名称
     * @return 部门id
     */
    Integer addDepartment(String departmentName);

    /**
     * 更新部门信息
     * @param departmentId 部门ID
     * @param newDepartmentName 新的部门名称
     * @return 是否更新成功
     */
    boolean updateDepartment(Integer departmentId, String newDepartmentName);

    /**
     * 分页查询部门
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @param departmentName 部门名称（用于条件查询）
     * @return 查询结果
     */
    Page<Department> searchDepartments(int pageNum, int pageSize, String departmentName);

    /**
     * 获取部门详情（包括部门下的员工）
     * @param departmentId 部门ID
     * @return 部门详情
     */
    Department getDepartmentDetails(Integer departmentId);

}
