package com.spt.managesystem.mapper;

import com.spt.managesystem.model.Overtime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author zhiyuan
* @description 针对表【overtime(加班表)】的数据库操作Mapper
* @createDate 2025-06-24 08:48:02
* @Entity com.spt.managesystem.model.Overtime
*/
@Mapper
public interface OvertimeMapper extends BaseMapper<Overtime> {

}




