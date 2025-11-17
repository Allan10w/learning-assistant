package com.itheima.config;

import com.itheima.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfigurer:配置类
 * 1.配置拦截器
 * 2.配置静态资源映射
 * 3.视图控制器
 * 4.跨域配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Autowired
//    private DemoInterceptor demoInterceptor;

    @Autowired
    private TokenInterceptor tokenInterceptor;
    //配置拦截器
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(demoInterceptor).addPathPatterns("/**");// 拦截所有请求
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")// 拦截所有请求
                .excludePathPatterns("/login");// 放行 /login 请求
    }

}
