package com.demo05.demo.service.impl;

import com.demo05.demo.dao.UserFileRepo;
import com.demo05.demo.model.FileInfo;
import com.demo05.demo.model.UserFile;
import com.demo05.demo.service.UserFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class UserFileServiceImp  { //implements UserFileService
    @Value("${prop.user-file}")
    private String userFile;

    @Resource
    private UserFileRepo userFileRepo;

    @Resource(name = "redisTem")
    private RedisTemplate<String,Object> rt;

    public void UserAddFile(FileInfo fileInfo, Long userId) {
        boolean b = userFileRepo.existsByFileIdAndUserId(fileInfo.getId(), userId);
        System.out.println(b);
        if (!b) {
            userFileRepo.save(new UserFile(userId, fileInfo.getId()));
        } else {
            //若用户想要上传的文件已经在回收站中，则直接从回收站移动到用户文件列表
            userFileRepo.setStatusFor(0,fileInfo.getId(),userId);
        }
    }

    public void userDelFile(Long fileId, Long userId) {
        userFileRepo.setStatusFor(1, fileId, userId);

    }

    public void userRestoreFile(Long fileId, Long userId) {
        userFileRepo.setStatusFor(0, fileId, userId);
    }

    public void userDelFileForever(Long fileId, Long userId) {
        userFileRepo.deleteByFileIdAndUserId(fileId,userId);
    }



}
