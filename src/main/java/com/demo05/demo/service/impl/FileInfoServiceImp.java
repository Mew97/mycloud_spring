package com.demo05.demo.service.impl;

import com.demo05.demo.dao.FileInfoRepo;
import com.demo05.demo.model.FileInfo;
import com.demo05.demo.service.FileInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileInfoServiceImp implements FileInfoService {
    @Resource
    private FileInfoRepo fileInfoRepo;

    @Override
    public FileInfo addFileInfo(FileInfo fileInfo) {
        return fileInfoRepo.save(fileInfo);
    }

    @Override
    public boolean checkFile(String name, String identifier) {
        return fileInfoRepo.existsByFilenameAndIdentifier(name, identifier);
    }

    @Override
    public FileInfo findOne(String name, String identifier) {
        return fileInfoRepo.getFirstByFilenameAndIdentifier(name, identifier);
    }

}
