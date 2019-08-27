package com.demo05.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    @Resource(name = "druidDataSource")
    private DataSource dataSource;

    @Bean
    public JdbcTemplate jt(){
        return new JdbcTemplate(dataSource);
    }

}
