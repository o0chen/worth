package com.blackeye.worth;

import com.blackeye.worth.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorthApplicationTests {

    @Autowired
    IUserService userService;

    @Test
    public void contextLoads() {
//        System.out.println(userService.findUserByName("admin").toString());
//        System.err.println(userService.get("1").toString());//userService 未实现的方法不可直接使用
    }

}
