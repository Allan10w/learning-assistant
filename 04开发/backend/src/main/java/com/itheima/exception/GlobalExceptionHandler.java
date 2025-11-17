package com.itheima.exception;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result handleException(Exception e){
        log.error("系统异常：", e);
        return Result.error("服务器忙，请稍后再试...");
    }
    @ExceptionHandler
    public Result handleDuplicateException(DuplicateKeyException e){
        log.error("系统异常：", e);
        String message = e.getMessage();
        int i = message.indexOf("Duplicate entry");
        String errMsg = message.substring(i);
        String[] arr = errMsg.split(" ");
        return Result.error(arr[2] + "已存在：");
    }
    @ExceptionHandler
    public Result handleDeptNotEmptyException(DeptNotEmptyException e){
        log.error("系统异常：", e);
        return Result.error(e.getMessage());
    }
}
