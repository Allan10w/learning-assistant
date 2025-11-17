package com.itheima.service;

import com.itheima.pojo.Clazz;

import java.util.List;

public interface ClazzService {
    /**
     * 查询所有班级信息
     * @return
     */
    List<Clazz> findAll();

    /**
     * 根据 ID 删除班级
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 新增班级
     * @param clazz
     */
    void addClazz(Clazz clazz);

    /**
     * 根据 ID 查询班级
     * @param id
     */
    Clazz getClazzById(Integer id);

    /**
     * 修改班级
     * @param clazz
     */
    void updateClazz(Clazz clazz);

    /**
     * 查询所有班级
     * @return
     */
    List<Clazz> getAll();
}
