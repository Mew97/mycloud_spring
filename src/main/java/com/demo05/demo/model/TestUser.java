package com.demo05.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TestUser implements Serializable {
    private String name;
    private int age;
    private String sex;
}
