package com.demo05.demo.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.demo05.demo.model.Chunk;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Stream;

@Slf4j
public class FileUtils {


    public static String generatePath(String uploadFolder, Chunk chunk){
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFolder).append("/").append(chunk.getIdentifier());

        if (!Files.isWritable(Paths.get(sb.toString()))){
            log.info("路径不存在，创建路径：{}", sb.toString());
            try{
                Files.createDirectories(Paths.get(sb.toString()));
            }catch (Exception e){
                log.error("文件夹已存在");
            }
        }
        return sb.append("/")
                .append(chunk.getFilename())
                .append("-")
                .append(chunk.getChunkNumber()).toString();
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static String getPrintSize(long size){
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    public static String mergeAndUpload(String targetFile, String folder, String filename){


        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageClient1 client = new StorageClient1(trackerServer, null);


            String fileID = client.upload_appender_file1(new byte[0], null, new NameValuePair[0]);

            Files.list(Paths.get(folder))
                    .filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1,o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            client.append_file1(fileID,Files.readAllBytes(path));
                            Files.delete(path);
                        } catch (Exception e){
                            log.error(e.getMessage(), e);
                        }
                    });

            deleteDir(new File(folder));  // 删除临时文件夹

            return fileID+"?attname="+filename;
        }catch (Exception e){
            e.printStackTrace();
            log.error("合并失败！");
            return null;
        }
    }

    public static void mergeAndUploadToOss(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String targetFile, String folder, String filename){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            Files.createFile(Paths.get(targetFile));
            Files.list(Paths.get(folder))
                    .filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1,o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            Files.delete(path);
                        } catch (Exception e){
                            log.error(e.getMessage(), e);
                        }
                    });
        }catch (Exception e){
            log.error("文件已存在");
        }
        ossClient.putObject(bucketName, filename, new File(targetFile));
        ossClient.shutdown();

    }

    public static void merge(String targetFile, String folder, String filename){
        try {
            Files.createFile(Paths.get(targetFile));
            Files.list(Paths.get(folder))
                    .filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1,o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            Files.delete(path);
                        } catch (Exception e){
                            log.error(e.getMessage(), e);
                        }
                    });
        }catch (Exception e){
            log.error("文件已存在");
        }
    }

}
