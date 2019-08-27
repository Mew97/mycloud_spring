package com.demo05.demo.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
public class FileInfo implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column
    private String identifier;

    @Column
    private Long totalSize;

    @Column
    private String type;

    @Column
    private String location;

    @Column
    private String date = getTime();

    @Column
    private String size;

    @Column
    private int status;

    //文件删除日期
    @Column
    private Long delTime;


    //添加一个格式化时间字段
    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }

    // 重写equals与hashcode方法，只要文件名与文件md5都相同就认为两个文件对象相等
    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof FileInfo){
            FileInfo file = (FileInfo) obj;
            return file.identifier.equals(this.identifier) && file.filename.equals(this.filename);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return filename.hashCode() * identifier.hashCode();
    }


}
