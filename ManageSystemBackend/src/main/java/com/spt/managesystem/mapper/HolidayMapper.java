package com.spt.managesystem.mapper;

import com.spt.managesystem.model.Holiday;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author zhiyuan
* @description 针对表【holiday(请假表)】的数据库操作Mapper
* @createDate 2025-06-25 16:54:12
* @Entity com.spt.managesystem.model.Holiday
*/
@Mapper
public interface HolidayMapper extends BaseMapper<Holiday> {

}




