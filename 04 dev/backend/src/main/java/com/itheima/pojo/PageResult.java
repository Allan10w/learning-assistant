package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果封装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private long total;
    private List<T> rows;

    public static <T> PageResult<T> of(List<T> rows){
        return new PageResult<>(rows.size(),rows);
    }
}
