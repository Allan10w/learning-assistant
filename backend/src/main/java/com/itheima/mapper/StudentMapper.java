package com.itheima.mapper;

import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {


    public List<Student> list(StudentQueryParam studentQueryParam);

    /**
     * 批量删除学员
     * @param ids
     */
    void deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 添加学员
     * @param student
     */
    void insert(Student student);

    /**
     * 根据id查询学员
     * @param id
     * @return
     */
    Student getById(Integer id);

    /**
     * 修改学员信息
     * @param student
     */
    void updateById(Student student);

    /**
     * 修改学员的违规次数和违规分数
     * @param id
     * @param score
     */
    void updateViolation(Integer id, Short score);

    /**
     * 统计各学历的学生人数
     */
    List<Map<String, Object>> getStuDegreeData();

    /**
     * 统计各班级的学生人数
     */
    List<Map<String, Object>> getStuCountData();
}
