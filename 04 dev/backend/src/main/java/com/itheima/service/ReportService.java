package com.itheima.service;

import com.itheima.pojo.ClazzOpiton;
import com.itheima.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {


    /**
     * 统计员工职位数据
     * @return
     */
    JobOption getEmpJobData();

    /**
     * 统计员工性别数据
     * @return
     */
    List<Map<String, Object>> getEmpGenderData();

    /**
     * 统计学生学历数据
     * @return
     */
    List<Map<String, Object>> getStuDegreeData();


    ClazzOpiton getStuCountData();
}
