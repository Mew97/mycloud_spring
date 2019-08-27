package com.demo05.demo.service.impl;

import com.demo05.demo.model.FileInfo;
import com.demo05.demo.service.UserFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

@SuppressWarnings("all")
@Service("inRedis")
public class RuserFileServiceImp implements UserFileService {
    @Value("${prop.user-file}")
    private String userFile;

    @Resource(name = "redisTem")
    private RedisTemplate<String,Object> rt;


    @Override
    public void userAddFile(Long userId,FileInfo fileInfo) {

        HashSet<FileInfo> o = (HashSet) rt.opsForValue().get(userFile + userId);
        if (o == null){
            HashSet<FileInfo> fileInfos = new HashSet<>();
            fileInfos.add(fileInfo);
            rt.opsForValue().set(userFile + userId, fileInfos);
        } else {
            //重写了FileInfo的equals与hashcode，Set会自动去重
            boolean add = o.add(fileInfo);
            System.out.println("是否已存在："+add);
            if (!add){
                o.forEach(f -> {if (f.getId().equals(fileInfo.getId())){
                    f.setStatus(0);
                    f.setDelTime(null);
                }});
            }
            rt.opsForValue().set(userFile + userId, o);
        }

    }

    @Override
    public void userDelFile(Long userId,FileInfo fileInfo) {

        HashSet<FileInfo> o = (HashSet)rt.opsForValue().get(userFile + userId);

        o.forEach(f -> {if (f.getId().equals(fileInfo.getId())){
            f.setStatus(1);
            f.setDelTime(new Date().getTime());
        }});
        rt.opsForValue().set(userFile + userId, o);
    }

    @Override
    public void userRestoreFile(Long userId,FileInfo fileInfo) {
        HashSet<FileInfo> o = (HashSet) rt.opsForValue().get(userFile + userId);
        o.forEach(f -> {if (f.getId().equals(fileInfo.getId())){
            f.setStatus(0);
            f.setDelTime(null);
        }});
        rt.opsForValue().set(userFile + userId, o);
    }

    @Override
    public void userDelFileForever(Long userId,FileInfo fileInfo) {
        HashSet<FileInfo> o = (HashSet) rt.opsForValue().get(userFile + userId);
        o.removeIf(file -> file.getId().equals(fileInfo.getId()));
        rt.opsForValue().set(userFile + userId, o);
    }

    @Override
    public ArrayList<FileInfo> getFileList(Long userId, int status) {
        ArrayList<FileInfo> fileInfos = new ArrayList<>();
        HashSet<FileInfo> o = (HashSet) rt.opsForValue().get(userFile + userId);

        if (o == null){
            return fileInfos;
        }

        //检查删除日期，过期文件直接删除
        o.removeIf(f -> f.getStatus()==1 && (new Date().getTime() - f.getDelTime()) > 30*24*60*60*1000L);

        o.forEach(f -> {if (f.getStatus()==status){fileInfos.add(f);}});
        rt.opsForValue().set(userFile + userId,o);
        return fileInfos;
    }
}
