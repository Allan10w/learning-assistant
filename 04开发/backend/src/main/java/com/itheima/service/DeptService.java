package com.itheima.service;

import com.itheima.pojo.Dept;

import java.util.List;

public interface DeptService {
    /**
     * 查询所有部门信息
     * @return
     */
    List<Dept> findAll();

    /**
     * 根据id删除部门
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 添加部门
     * @param dept
     */
    void add(Dept dept);

    /**
     * 根据id查询部门
     */
    Dept getById(Integer id);


    /**
     *  修改部门
     */
    void update(Dept dept);

    /**
    * 删除部门前校验部门下是否有员工
     */
    int countEmpByDeptId(Integer deptId);
}
