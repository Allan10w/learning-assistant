package com.itheima.controller;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/students")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 学院列表查询，该接口用于学员列表数据的条件分页查询
     */
    @GetMapping
    public Result list(StudentQueryParam studentQueryParam){
        log.info("查询所有学员信息/分页查询:{}",studentQueryParam);
        PageResult<Student> pageResult = studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 删除学员
     */
    @DeleteMapping("/{ids}")
    public Result deleteStuById(@PathVariable List<Integer> ids){
        log.info("删除学员：{}",ids);
        studentService.delete(ids);
        return Result.success();
    }

    /**
     * 新增学员
     */
    @PostMapping
    public Result save(@RequestBody Student student){
        log.info("新增学员：{}",student);
        studentService.save(student);
        return Result.success();
    }

    /**
     * 根据 id 查询学生信息
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        log.info("根据 id 查询学生信息：{}",id);
        Student student = studentService.getInfo(id);
        return Result.success(student);
    }

    /**
     * 修改学员信息
     */
    @PutMapping
    public Result update(@RequestBody Student student){
        log.info("修改学员信息:{}",student);
        studentService.update(student);
        return Result.success();
    }
    /**
     * 违纪处理：修改学员违纪数据
     */
    @PutMapping("/violation/{id}/{score}")
    public Result updateviolation( @PathVariable Integer id, @PathVariable Short score){
        log.info("修改学员违纪数据 id={},score={}",id,score);
        studentService.updateViolation(id,score);
        return Result.success();

    }
}
