package com.itheima.pojo;

import lombok.Data;

// 查询条件对象（非必须，全是可选）
@Data
public class LogQuery {
    private String returnValue;     // 模糊匹配
    private Long   costTime;        // >=
    private String operateEmpName;  // 模糊匹配
}