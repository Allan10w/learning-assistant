package com.itheima.controller;

import com.itheima.anno.Log;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工管理Controller
 */
@Slf4j
@RestController
@RequestMapping("/emps")
@RequiredArgsConstructor
public class EmpController {

    private final EmpService empService;

    /**
     * 分页查询
     */
    @GetMapping
    public Result page(EmpQueryParam empQueryParam) {
        log.info("分页查询：{}", empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 新增员工
     *
     * @return
     */
    @Log
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("新增员工:{}", emp);
        empService.save(emp);
        return Result.success();
    }

    /**
     * 员工删除-List集合
     *
     * @return
     */
    @Log
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("员工删除:{}", ids);
        empService.delete(ids);
        return Result.success();
    }

    /**
     * 根据 id 查询员工信息
     *
     * @return
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("查询员工信息：{}", id);
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    /**
     * 员工修改
     *
     * @return
     */
    @Log
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("员工修改:{}", emp);
        empService.update(emp);
        return Result.success();
    }
}
