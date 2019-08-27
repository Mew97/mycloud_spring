package com.demo05.demo.service;

import com.demo05.demo.model.FileInfo;


public interface FileInfoService {
    FileInfo addFileInfo(FileInfo fileInfo);
    boolean checkFile(String name,String identifier);
    FileInfo findOne(String name,String identifier);
}
