package com.demo05.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 32,unique = true)
    private String name;
    @Column(length = 64)
    private String sessionId;
    @Column(length = 64)
    private String pwd;
    @Column(length = 20)
    private String phoneNum;
    @Column(length = 32)
    private String uuid;

    public User(String name, String sessionId, String pwd) {
        this.name = name;
        this.sessionId = sessionId;
        this.pwd = pwd;
    }

}
