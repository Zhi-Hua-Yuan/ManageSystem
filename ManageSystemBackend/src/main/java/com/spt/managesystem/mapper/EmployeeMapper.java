package com.spt.managesystem.mapper;

import com.spt.managesystem.model.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author zhiyuan
* @description 针对表【employee(员工表)】的数据库操作Mapper
* @createDate 2025-06-16 19:34:13
* @Entity generator.domain.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




