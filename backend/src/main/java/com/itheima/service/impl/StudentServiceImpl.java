package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;



@Service
public class StudentServiceImpl implements StudentService {


    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public PageResult<Student> page(StudentQueryParam studentQueryParam) {
        //设置分页参数（pageHelper）
        PageHelper.startPage(studentQueryParam.getPage(),studentQueryParam.getPageSize());

        //执行查询
        List<Student> studentList = studentMapper.list(studentQueryParam);

        //解析查询结果，并封装数据
        Page<Student> s = (Page<Student>) studentList;
        return new PageResult<Student>(s.getTotal(),studentList);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void delete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()){
            throw new IllegalArgumentException("ids不能为空");
        }
        studentMapper.deleteByIds(ids);
    }

    /**
     * 新增学员
     * @param student
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void save(Student student) {
        if (student == null){
            throw new IllegalArgumentException("学生不能为空");
        }
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    @Override
    public Student getInfo(Integer id) {
        return studentMapper.getById(id);
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.updateById(student);
    }

    @Override
    public void updateViolation(Integer id, Short score) {
        if (id==null){
            throw new IllegalArgumentException("id不能为空");
        }
        if (score == null || score<0){
            throw new IllegalArgumentException("score不能为空且为非负数");
        }
        studentMapper.updateViolation(id,score);
    }
}
