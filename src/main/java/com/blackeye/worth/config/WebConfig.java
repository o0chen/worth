package com.blackeye.worth.config;

import com.blackeye.worth.core.params.CustomerArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig  implements WebMvcConfigurer {

    @Autowired
    private CustomerArgumentResolver customerArgumentResolver;

    /**
     * 配置ArgumentResolvers
     * 实现采用业务逻辑，向controllor 方法中注入参数
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(0,customerArgumentResolver);
    }
}

