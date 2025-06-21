package com.spt.managesystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spt.managesystem.model.Department;
import com.spt.managesystem.service.DepartmentService;
import com.spt.managesystem.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;

/**
* @author zhiyuan
* @description 针对表【department(部门表)】的数据库操作Service实现
* @createDate 2025-06-16 19:39:01
*/
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
    implements DepartmentService{

}




