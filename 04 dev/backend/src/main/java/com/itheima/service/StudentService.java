package com.itheima.service;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;

import java.util.List;

public interface StudentService {


    /**
     * 分页查询方法
     * @param studentQueryParam
     * @return
     */
    PageResult<Student> page(StudentQueryParam studentQueryParam);

    void delete(List<Integer> ids);

    /**
     * 新增学员
     * @param student
     */
    void save(Student student);

    /**
     * 根据id查询学员信息
     * @param student
     */
    Student getInfo(Integer id);

    /**
     * 修改学员信息
     * @param student
     */
    void update(Student student);

    void updateViolation(Integer id, Short score);
}
