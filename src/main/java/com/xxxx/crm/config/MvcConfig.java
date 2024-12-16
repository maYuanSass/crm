package com.xxxx.crm.config;

import com.xxxx.crm.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public NoLoginInterceptor noLoginInterceptor() {
        return new NoLoginInterceptor();
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(noLoginInterceptor())
                //设置需要拦截的资源（默认拦截所有的资源）
                .addPathPatterns("/**")
                //设置不需要被拦截的资源
                .excludePathPatterns("/css/**","/images/**","/js/**","/lib/**", "/index","/user/login");
    }

}
