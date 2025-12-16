package com.itheima.aop;

import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.OperateLog;
import com.itheima.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class OperateLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.itheima.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 创建操作日志对象
        OperateLog operateLog = new OperateLog();

        try {
            // 获取目标类名和方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();

            // 获取方法参数
            String methodParams = Arrays.toString(joinPoint.getArgs());

            // 执行目标方法
            Object result = joinPoint.proceed();

            // 记录结束时间
            long endTime = System.currentTimeMillis();

            // 获取操作人ID
            Integer operateEmpId = getCurrentUserId();

            // 设置操作日志信息
            operateLog.setOperateEmpId(operateEmpId);
            operateLog.setOperateTime(LocalDateTime.now());
            operateLog.setClassName(className);
            operateLog.setMethodName(methodName);
            operateLog.setMethodParams(methodParams);
            operateLog.setReturnValue(result != null ? result.toString() : "无返回值");
            operateLog.setCostTime(endTime - startTime);

            // 保存操作日志
            log.info("记录操作日志：{}",operateLog);
            operateLogMapper.insert(operateLog);

            return result;
        } catch (Exception e) {
            // 记录异常情况
            long endTime = System.currentTimeMillis();
            operateLog.setCostTime(endTime - startTime);
            operateLog.setReturnValue("执行异常：" + e.getMessage());
            operateLogMapper.insert(operateLog);
            throw e;
        }
    }

    /**
     * 从请求头中获取当前操作人ID
     * @return 操作人ID
     */
    private Integer getCurrentUserId() {
        return CurrentHolder.getCurrentId();
    }
}
