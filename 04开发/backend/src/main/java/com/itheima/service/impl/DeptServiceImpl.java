package com.itheima.service.impl;

import com.itheima.exception.DeptNotEmptyException;
import com.itheima.mapper.DeptMapper;
import com.itheima.pojo.Dept;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        //检查部门下是否有员工
        int empCount = deptMapper.countEmpByDeptId(id);
        if (empCount>0){
            throw new DeptNotEmptyException("部门下有员工，不能删除");
        }
        deptMapper.deleteById(id);
    }

    @Override
    public void add(Dept dept) {
        //1.补全基础属性-createTime,updateTime
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        //2.调用 Mapper 接口方法插入数据
        deptMapper.add(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    @Override
    public void update(Dept dept) {
        //1.补全基础属性-updateTime
        dept.setUpdateTime(LocalDateTime.now());
        //2.调用 Mapper
        deptMapper.update(dept);
    }

    @Override
    public int countEmpByDeptId(Integer deptId) {
        return deptMapper.countEmpByDeptId(deptId);
    }
}
