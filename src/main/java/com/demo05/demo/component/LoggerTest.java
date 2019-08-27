package com.demo05.demo.component;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
//@Component
public class LoggerTest {
    @Pointcut("execution(* com.demo05.demo.controller.*.*(..))")
    public void pc(){}

    @Before("pc()")
    public void startRecord(){
        System.out.println("start record");
    }

    @After("pc()")
    public void endRecord(){
        System.out.println("end record");
    }
}
