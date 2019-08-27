package com.demo05.demo.controller;

import com.demo05.demo.annotation.UserLoginToken;
import com.demo05.demo.model.FileInfo;
import com.demo05.demo.service.FileInfoService;

import com.demo05.demo.service.UserFileService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;


@RestController
@RequestMapping("api/")
public class FileController {

    @Resource(name = "inRedis")
    private UserFileService userFileService;

    @UserLoginToken
    @GetMapping(value = "fileList", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ArrayList getFileList(Long userId, int status){
        return userFileService.getFileList(userId, status);
    }

    @UserLoginToken
    @PostMapping("deleteFile")
    public boolean deleteFile(FileInfo fileInfo, Long userId){
        userFileService.userDelFile(userId,fileInfo);
        return true;
    }

    @UserLoginToken
    @PostMapping("restoreFile")
    public boolean restoreFile(FileInfo fileInfo, Long userId){
        userFileService.userRestoreFile(userId,fileInfo);
        return true;
    }

    @UserLoginToken
    @PostMapping("delFileForever")
    public boolean delFileForever(FileInfo fileInfo, Long userId){
        userFileService.userDelFileForever(userId,fileInfo);
        return true;
    }

}
