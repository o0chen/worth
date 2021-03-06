package com.blackeye.worth;

import com.blackeye.worth.core.customer.BaseRepositoryFactoryBean;
import com.blackeye.worth.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
//@EnableAutoConfiguration 自动配置jar
@EnableWebMvc
//public class WorthApplication extends WebMvcConfigurerAdapter {
public class WorthApplication{

    public static void main(String[] args) {
        ApplicationContext app=SpringApplication.run(WorthApplication.class, args);
        SpringContextUtil.setApplicationContext(app);
    }

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        super.addArgumentResolvers(argumentResolvers);
//        argumentResolvers.add(new CustomerArgumentResolver());
//    }

}
