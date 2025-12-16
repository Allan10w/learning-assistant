package com.itheima.service.impl;

import com.itheima.mapper.EmpMapper;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.ClazzOpiton;
import com.itheima.pojo.JobOption;
import com.itheima.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public JobOption getEmpJobData() {
        //1.调用 Mapper 接口获取统计数据
        List<Map<String, Object>> list = empMapper.countEmpJobData();//map:pos=校验主管，num=1
        //2.封装返回
        List<Object> jobList = list.stream()
                .map(dataMap -> dataMap.get("pos")).
                toList();
        List<Object> dataList = list.stream()
                .map(dataMap -> dataMap.get("num"))
                .toList();
        return new JobOption(jobList,dataList);
    }

    /**
     * 统计员工性别数据
     */
    @Override
    public List<Map<String, Object>> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }

    @Override
    public List<Map<String, Object>> getStuDegreeData() {
        return studentMapper.getStuDegreeData();
    }
/*
    @Override
    public ClazzOpiton getStuCountData() {
        //吊用 mapper 接口，获取数据
        List<Map<String, Object>> stuCountDataList = studentMapper.getStuCountData();
        //组装结果并返回
        List<Object> clazzList = stuCountDataList.stream().map(dataMap -> dataMap.get("clazzName")).toList();
        List<Object> dataList = stuCountDataList.stream().map(dataMap -> dataMap.get("studentCount")).toList();
        return new ClazzOpiton(clazzList,dataList);
    }*/
    @Override
    public ClazzOpiton getStuCountData() {
        List<Map<String, Object>> rows = studentMapper.getStuCountData();

        List<String> clazzList = rows.stream()
                .map(r -> String.valueOf(r.getOrDefault("clazzName", "")))
                .toList();

        List<Integer> dataList = rows.stream()
                .map(r -> {
                    Object n = r.get("studentCount");
                    return n == null ? 0 : ((Number) n).intValue();
                })
                .toList();

        return new ClazzOpiton(clazzList, dataList);
    }
}
