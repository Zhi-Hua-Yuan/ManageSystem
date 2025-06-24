package com.spt.managesystem.controller;

import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.common.ResultUtils;
import com.spt.managesystem.exception.BusinessException;
import com.spt.managesystem.model.Department;
import com.spt.managesystem.model.Employee;
import com.spt.managesystem.service.DepartmentService;
import com.spt.managesystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.spt.managesystem.common.ErrorCode.NOT_LOGIN;
import static com.spt.managesystem.common.ErrorCode.NO_AUTH;

/**
 * @author songpintong
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;
    @Resource
    private EmployeeService employeeService;

    /**
     * 添加部门
     *
     * @param departmentName 部门名称
     * @return 部门ID
     */
    @PostMapping("/add")
    public BaseResponse<Integer> addDepartment(String departmentName, HttpServletRequest request) {
        //只有系统管理员可以添加部门
        if (!employeeService.isAdmin(request)) {
            throw new BusinessException(NO_AUTH);
        }
        int result = departmentService.addDepartment(departmentName);
        return ResultUtils.success(result);
    }

    /**
     * 更新部门信息
     *
     * @param departmentId      部门ID
     * @param newDepartmentName 新的部门名称
     * @return 更新结果
     */
    @PutMapping("/update")
    public BaseResponse<Boolean> updateDepartment(@RequestParam Integer departmentId,
                                                  @RequestParam String newDepartmentName,
                                                  HttpServletRequest request) {
        // 只有系统管理员可以更新部门
        if (!employeeService.isAdmin(request)) {
            throw new BusinessException(NO_AUTH);
        }
        boolean result = departmentService.updateDepartment(departmentId, newDepartmentName);
        return ResultUtils.success(result);
    }

    /**
     * 删除部门
     *
     * @param departmentId 部门ID
     * @return 删除结果
     */
    @PostMapping("/delete/{departmentId}")
    public BaseResponse<Boolean> deleteDepartment(@PathVariable Integer departmentId,
                                                  HttpServletRequest request) {
        // 只有系统管理员可以删除部门
        if (!employeeService.isAdmin(request)) {
            throw new BusinessException(NO_AUTH);
        }
        if (departmentId <= 0) {
            return ResultUtils.success(false);
        }
        boolean result = departmentService.removeById(departmentId);
        return ResultUtils.success(result);
    }

    /**
     * 分页查询部门
     *
     * @param pageNum        当前页码
     * @param pageSize       每页大小
     * @param departmentName 部门名称（用于条件查询）
     * @return 查询结果
     */
    @GetMapping("/search")
    public BaseResponse<List<Department>> searchDepartments(@RequestParam int pageNum,
                                                            @RequestParam int pageSize,
                                                            @RequestParam(required = false) String departmentName,
                                                            HttpServletRequest request) {
        // 所有登录用户都可以查看部门列表
        Employee employee = employeeService.getLoginEmployee(request);
        if (employee == null) {
            throw new BusinessException(NOT_LOGIN);
        }

        List<Department> departments = departmentService.searchDepartments(pageNum, pageSize, departmentName);
        return ResultUtils.success(departments);
    }

    /**
     * 获取部门详情（包括部门下的员工）
     *
     * @param departmentId 部门ID
     * @return 部门详情
     */
    @GetMapping("/details/{departmentId}")
    public BaseResponse<Department> getDepartmentDetails(@PathVariable Integer departmentId,
                                                         HttpServletRequest request) {
        // 所有登录用户都可以查看部门详情
        Employee employee = employeeService.getLoginEmployee(request);
        if (employee == null) {
            throw new BusinessException(NOT_LOGIN);
        }

        Department department = departmentService.getDepartmentDetails(departmentId);
        return ResultUtils.success(department);
    }


}
