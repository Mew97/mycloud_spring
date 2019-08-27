package com.demo05.demo.dao;

import com.demo05.demo.model.FileInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface FileInfoRepo extends JpaRepository<FileInfo,Long> {
    boolean existsByFilenameAndIdentifier(String name, String identifier);

    FileInfo getFirstByFilenameAndIdentifier(String name, String identifier);

    @Query("select f from FileInfo f where f.id in (:ids)")
    ArrayList<FileInfo> findByIdIn(@Param(value = "ids") List<Long> ids);
}
