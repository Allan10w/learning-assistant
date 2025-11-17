package com.itheima.mapper;

import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 员工工作经历
 */
@Mapper
public interface EmpExprMapper {
    /**
     * 批量保存员工工作经历
     * @param exprList
     */
    void insertBatch(List<EmpExpr> exprList);

    /**
     * 根据员工id批量删除员工工作经历
     * @param ids
     */
    void deleteByEmpIds(List<Integer> empIds);
}
