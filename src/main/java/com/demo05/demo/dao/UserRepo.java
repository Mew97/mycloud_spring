package com.demo05.demo.dao;

import com.demo05.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface UserRepo extends JpaRepository<User,Long> {
    List<User> findByNameAndPwd(String name, String pwd);

    User getFirstByNameAndPwd(String name, String pwd);

    User getFirstByPhoneNum(String phoneNum);

    @Modifying
    @Query("update User set sessionId = ?1 where name = ?2")
    int setSessionByName(String sessionId, String name);

    @Modifying
    @Query("update User set uuid = ?1 where name = ?2")
    int setUuidByName(String uuid, String name);

    @Modifying
    @Query("update User set pwd = ?1 where name = ?2")
    int setPwdByName(String pwd, String name);

    boolean existsByName(String name);

    boolean existsBySessionId(String sessionId);

    boolean existsByPhoneNum(String phoneNum);

}
