package com.itheima.mapper;

import com.itheima.pojo.Clazz;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClazzMapper {
    /**
     * 查询所有班级信息
     */
    List<Clazz> findAll();

    @Delete("delete from clazz where id = #{id}")
    void deleteById(Integer id);

    /**
     * 新增班级
     */
    @Insert("insert into clazz(name,room,begin_date,end_date,master_id,subject,create_time,update_time) values (#{name},#{room},#{beginDate},#{endDate},#{masterId},#{subject},#{createTime},#{updateTime}) ")
    void addClazz(Clazz clazz);

    /**
     * 根据 ID 查询班级
     *
     * @return
     */
    @Select("select * from clazz where id = #{id}")
    Clazz getClazzById(Integer id);

    /**
     * 修改班级
     * @param clazz
     */
    @Update("update clazz set name = #{name}, room = #{room}, begin_date = #{beginDate}, end_date = #{endDate}, master_id = #{masterId}, subject = #{subject}, update_time = #{updateTime} where id = #{id} ")
    void updateClazz(Clazz clazz);

    /**
     * 查询所有班级
     * @return
     */
    @Select("SELECT id, name, room, begin_date, end_date, master_id, subject, create_time, update_time from clazz;")
    List<Clazz> getAll();


}
