package com.itheima.interceptor;

import com.itheima.utils.CurrentHolder;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Description: 令牌拦截器
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //1.获取请求路径
//        String requestURI = request.getRequestURI();
//
//        //2.判断是否是登录请求，如果路径中包含了/login，说明是登录请求，放行
//        if (requestURI.contains("/login")){
//            log.info("登录请求，放行");
//            return true;
//        }

        //3.获取请求头中的token
        String token = request.getHeader("token");

        //4.判断token是否存在,如果不存在，说明用户没有登录，返回错误信息
        if (token == null || token.isEmpty()){
            log.info("令牌为空，拦截请求，响应 401...");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //5.如果 token 存在，校验令牌，如果校验失败，返回错误信息（响应 401状态码）
        try{
            Claims claims = JwtUtils.parseToken(token);//调用工具类解析令牌，从令牌中获取载荷
            //从载荷中获取员工ID，并存入 CurrentHolder
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentId(empId);//存入当前线程
            log.info("当前登录员工ID：{}，将其存入 ThreadLocal",empId);
        }catch (Exception e){
            log.info("非法令牌，响应 401...");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //6.如果校验成功，放行
        log.info("令牌合法，放行...");
        return true;
    }

    /**
     * 重写 afterCompletion 方法，在请求处理完成后执行 把当前线程的员工ID移除 的操作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //请求处理完成后，移除当前线程的员工ID
        CurrentHolder.remove();
        log.info("请求处理完成，移除当前线程的员工ID");
    }
}
