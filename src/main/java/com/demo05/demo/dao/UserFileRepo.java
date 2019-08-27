package com.demo05.demo.dao;

import com.demo05.demo.model.UserFile;
import com.demo05.demo.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserFileRepo extends CrudRepository<UserFile, Long> {
    boolean existsByFileIdAndUserId(Long fileId, Long userId);

    List<UserFile> findByUserId(Long userId);

    List<UserFile> findByUserIdAndStatus(Long userId, int status);

    @Modifying
    @Query("update UserFile u set u.status = ?1 where u.fileId=?2 and u.userId=?3")
    void setStatusFor(int status,Long fileId, Long userId);

    int deleteByFileIdAndUserId(Long fileId, Long userId);


}
