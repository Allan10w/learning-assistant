package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 员工信息
 */
@Mapper
public interface EmpMapper {

    /**
     * 查询所有员工信息
     */
    //@Select("select e.*, d.name deptName from emp e left join dept d on e.dept_id = d.id order by e.update_time desc")
    public List<Emp> list(EmpQueryParam empQueryParam);

    /**
     * 新增员工基本信息
     *
     * @param emp
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")//获取到生成的主键--主键返回
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time)\n" +
            "VALUES (#{username}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);


    /**
     * 根据 id批量删除员工信息
     *
     * @param ids
     */

    void deleteByIds(List<Integer> ids);

    /**
     * 根据 id查询员工信息
     *
     * @param id
     */
    Emp getById(Integer id);

    /**
     * 根据 id更新员工信息
     *
     * @param emp
     */
    void updateById(Emp emp);

    /**
     * 统计各职位的员工人数
     */

    List<Map<String, Object>> countEmpJobData();

    /**
     * 统计各性别的员工人数
     */

    List<Map<String, Object>> countEmpGenderData();


    /**
     * 根据用户名和密码查询员工信息
     *
     * @param emp
     * @return
     */
    @Select("select id,username,name from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(Emp emp);
}
