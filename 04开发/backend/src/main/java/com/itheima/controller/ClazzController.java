package com.itheima.controller;

import com.itheima.anno.Log;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clazzs")
@Slf4j
public class ClazzController {

    //注入 Service
    @Autowired
    private ClazzService clazzService;

    @GetMapping
    public Result list(){
        log.info("查询全部班级数据");
        List<Clazz> clazzList = clazzService.findAll();
        PageResult<Clazz> pageResult = PageResult.of(clazzList);
        return Result.success(pageResult);
    }

    /**
     * 根据 ID 删除班级
     */
    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("根据 id 删除班级：{}",id);
        clazzService.deleteById(id);
        return Result.success();
    }

    /**
     * 新增班级
     */
    @Log
    @PostMapping
    public Result add(@RequestBody Clazz clazz){
        log.info("新增班级：{}",clazz);
        clazzService.addClazz(clazz);
        return Result.success();
    }
    /**
     * 根据 ID 查询班级
     */
    @GetMapping("/{id:\\d+}")
    public Result getById(@PathVariable Integer id){
        log.info("根据 ID 查询班级：{}",id);
        Clazz clazz = clazzService.getClazzById(id);
        return Result.success(clazz);
    }
    /**
     * 修改班级
     */
    @Log
    @PutMapping
    public Result update(@RequestBody Clazz clazz){
        log.info("修改班级：{}",clazz);
        clazzService.updateClazz(clazz);
        return Result.success();
    }

    /**
     * 查询所有班级
     */
    @GetMapping("/list")
    public Result getAll(){
        log.info("查询所有班级");
        List<Clazz> ClazzList = clazzService.getAll();
        return Result.success(ClazzList);
    }



}
