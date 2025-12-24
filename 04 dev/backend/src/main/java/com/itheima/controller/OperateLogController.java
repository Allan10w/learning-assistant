package com.itheima.controller;

import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.OperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OperateLogController {

    private final OperateLogService operateLogService;

    @GetMapping("/log/page")
    public Result page(
            @RequestParam Integer page,
            @RequestParam Integer pageSize) {
        PageResult<OperateLog> data = operateLogService.page(page, pageSize);
        return Result.success(data);
    }
}