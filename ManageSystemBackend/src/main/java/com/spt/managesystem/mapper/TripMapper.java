package com.spt.managesystem.mapper;

import com.spt.managesystem.model.Trip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author zhiyuan
* @description 针对表【trip(出差表)】的数据库操作Mapper
* @createDate 2025-06-26 16:23:17
* @Entity com.spt.managesystem.model.Trip
*/
@Mapper
public interface TripMapper extends BaseMapper<Trip> {

}




