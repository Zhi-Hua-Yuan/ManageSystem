package com.spt.managesystem.controller;

import com.spt.managesystem.common.BaseResponse;
import com.spt.managesystem.common.ResultUtils;
import com.spt.managesystem.model.Overtime;
import com.spt.managesystem.service.OvertimeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author songpintong
 */
@RestController
@RequestMapping("/overtime")
public class OvertimeController {

    @Resource
    private OvertimeService overtimeService;

    /**
     * 添加加班申请
     * @param overtime
     * @return
     */
    @PostMapping("add")
    public BaseResponse<Integer> addOvertime(@RequestBody Overtime overtime){
        int result = overtimeService.addOvertime(overtime);
        return ResultUtils.success(result);
    }

    /**
     * 获取加班申请详情
     * @param overtimeId
     * @return
     */
    @GetMapping("/details/{overtimeId}")
    public BaseResponse<Overtime> getOvertimeDetails(@PathVariable Integer overtimeId){
        Overtime overtime = overtimeService.getById(overtimeId);
        return ResultUtils.success(overtime);
    }

    /**
     * 条件分页查询加班申请列表
     * @param pageNum
     * @param pageSize
     * @param overtimeEmpId
     * @param overtimeEmpName
     * @param overtimeDeptName
     * @param overtimeDate
     * @param isPass
     * @param createTime
     * @param updateTime
     * @return
     */
}












