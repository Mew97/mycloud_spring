package com.demo05.demo;

import com.demo05.demo.dao.FileInfoRepo;
import com.demo05.demo.dao.UserFileRepo;
import com.demo05.demo.dao.UserRepo;
import com.demo05.demo.model.FileInfo;
import com.demo05.demo.model.User;
import com.demo05.demo.model.UserFile;
import com.demo05.demo.service.FileInfoService;
import com.demo05.demo.service.UserFileService;
import com.demo05.demo.service.UserService;
import com.demo05.demo.util.Sms;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private FileInfoRepo fileInfoRepo;

    @Autowired
    private UserFileRepo userFileRepo;

    @Autowired
    private UserFileService userFileService;

//    @Autowired
//    private StringRedisTemplate red;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("redisTem")
    private RedisTemplate<String, Object> red;

    @Autowired
    private JdbcTemplate jt;

    @Test
    public void contextLoads() {

        User user = new User();
        user.setName("zhubo");
        user.setPwd("123");
        //userRepo.save(user);

        Iterable<User> all = userRepo.findAll();

        boolean a = userRepo.existsByName("zhubo");
        all.forEach(System.out::println);
        System.out.println(a);
        //int i = userRepo.setSessionByName("qweqweqweqw", "zhubo");
    }

    @Test
    public void testFileCheck(){
        User zhubo = userRepo.getFirstByNameAndPwd("zhubo", "123");
        zhubo.setName("zhubo2");
        System.out.println(zhubo);
    }

    @Test
    public void fastDfs(){
        String a = "group1/M00/00/00/wKgCal0IwrSAQoibAAAAQwXOwT43174.js";

        String b = "group1/M00/00/00/wKgCHl0JQXeAYKhuAAAAQwXOwT43703.js";

        String c = "group1/M00/00/00/wKgCL10JWuCEBt4DAAAAAAXOwT46066066";

        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);

            String local_filename = "/Users/zhubo/Desktop/datatable.js";

            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);

            NameValuePair[] metaList = new NameValuePair[1];
            metaList[0] = new NameValuePair("fileName", "file1");
            //String fileId = client.upload_file1(local_filename, null, metaList);
            //System.out.println("upload success. file id is: " + fileId);
            //System.out.println("upload success. file id is: " + fileId);
            //FileInfo fileInfo = client.query_file_info1(a);
            byte[] bytes1 = client.download_file1(a);
            System.out.println(Arrays.toString(bytes1));
            byte[] bytes = client.download_file1(b);
            System.out.println(Arrays.toString(bytes));

            byte[] bytes2 = client.download_file1(c);
            System.out.println(Arrays.toString(bytes2));

            int i = client.append_file1(c, bytes);

            byte[] bytes3 = client.download_file1(c);
            System.out.println(Arrays.toString(bytes3));


            //String s = client.upload_appender_file1(bytes, null, metaList);
            System.out.println(i);

            trackerServer.close();



        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @Test
    public void testRedis(){
        HashSet<FileInfo> fileInfos = new HashSet<>();
        FileInfo fileInfo = new FileInfo();
        FileInfo fileInfo1 = new FileInfo();

        fileInfo.setFilename("aaa");
        fileInfo1.setFilename("aaa");

        fileInfo.setIdentifier("111");
        fileInfo1.setIdentifier("121");

        fileInfo.setId(1L);
        fileInfo1.setId(2L);

        fileInfo.setStatus(0);
        fileInfo1.setStatus(1);

        //fileInfos.add(fileInfo);
        //fileInfos.add(fileInfo1);
        //red.opsForValue().set("q",fileInfos);
        System.out.println(fileInfo);
        System.out.println(fileInfo1);

        //userFileService.rUserAddFile(1L,fileInfo);
        //userFileService.rUserAddFile(1L,fileInfo1);
        //userFileService.rUserRestoreFile(1L,fileInfo1);
        //userFileService.rUserRestoreFile(1L,fileInfo);
        ArrayList<FileInfo> fileInfos1 = userFileService.getFileList(1L, 0);

        //HashSet<FileInfo> q = (HashSet)red.opsForValue().get("api:u_f:1");

        System.out.println(fileInfos1);


    }

    @Test
    public void testTime(){
        System.out.println(FileInfo.getTime());
        int a = jt.queryForObject("select count(*) from user", Integer.class);
        System.out.println(a);
        List<User> list = jt.query("select id,name,pwd from user", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                User user = new User();
                user.setId(rs.getLong(1));
                user.setName(rs.getString(2));
                user.setPwd(rs.getString(3));
                return user;
            }
        });
        System.out.println(list);
    }

    @Test
    public void testSms(){
        User user = new User();
        user.setName("aaa");
        user.setPwd("123");
        user.setPhoneNum("123344123");
        userRepo.save(user);
        //System.out.println(userRepo.existsByPhoneNum(null));
    }

}
