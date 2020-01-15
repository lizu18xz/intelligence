package com.fayayo.inte;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author dalizu on 2020/1/1.
 * @version v1.0
 * @desc
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
@MapperScan("com.fayayo.inte.dao")
public class InteApplication {

    public static void main(String[] args) {
        SpringApplication.run(InteApplication.class,args);
    }


}
