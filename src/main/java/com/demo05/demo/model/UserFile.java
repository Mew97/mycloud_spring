package com.demo05.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
public class UserFile implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 64)
    private Long userId;

    @Column(length = 64)
    private Long fileId;

    @Column(length = 2)
    private int status = 0;


    public UserFile(Long userId, Long fileId) {
        this.userId = userId;
        this.fileId = fileId;
    }
}
