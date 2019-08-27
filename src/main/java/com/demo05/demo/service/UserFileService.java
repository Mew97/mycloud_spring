package com.demo05.demo.service;

import com.demo05.demo.model.FileInfo;

import java.util.ArrayList;

public interface UserFileService {

    //改用redis存储用户文件列表
    void userAddFile(Long userId,FileInfo fileInfo);
    void userDelFile(Long userId,FileInfo fileInfo);
    void userRestoreFile(Long userId,FileInfo fileInfo);
    void userDelFileForever(Long userId,FileInfo fileInfo);
    ArrayList<FileInfo> getFileList(Long userId, int status);

}
