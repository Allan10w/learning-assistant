package com.itheima.mapper;

import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper // 表示当前接口被 MyBatis 扫描，创建代理对象，将自动创建的 Dao 接口的实现类注入到 Spring 容器中
public interface DeptMapper {

    /**
     * 查询所有部门信息
     */
    //手动结果映射，方法一
//    @Results({
//            @Result(column = "create_time",property = "createTime"),
//            @Result(column = "update_time",property = "updateTime")
//    })
    //手动结果映射,起别名，方法二
//    @Select("select dept.id, dept.name, dept.create_time createTime, dept.update_time updateTime from dept order by dept.update_time desc ;")
    @Select("select dept.id, dept.name, dept.create_time, dept.update_time from dept order by dept.update_time desc ;")
    List<Dept> findAll();

    /**
     * 根据 id 删除部门
     */
    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);

    @Insert("insert into dept(name,create_time,update_time) values (#{name},#{createTime},#{updateTime})")
    void add(Dept dept);

    /**
     * 根据 id 查询部门
     */
    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);

    /**
     * 更新部门
     * @param dept
     */
    @Update("update dept set name = #{name}, update_time = #{updateTime} where id = #{id} ")
    void update(Dept dept);

    /**
     * 根据部门 id 查询该部门下的员工数量
     */
    @Select("select count(*) from emp where dept_id = #{deptId}")
    int countEmpByDeptId(Integer deptId);
}
