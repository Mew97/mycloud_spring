package com.demo05.demo.controller;

import com.demo05.demo.annotation.UserLoginToken;
import com.demo05.demo.model.Chunk;
import com.demo05.demo.model.FileInfo;
import com.demo05.demo.service.ChunkService;
import com.demo05.demo.service.FileInfoService;
import com.demo05.demo.service.UserFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.demo05.demo.util.FileUtils.*;

@RestController
@RequestMapping("api/")
public class UploadController {
    @Value("${prop.upload-folder}")
    private String uploadFolder;

    @Value("${prop.download-addr}")
    private String downLoadAddr;

    @Value("${prop.endpoint}")
    private String endpoint;

    @Value("${prop.accessKeyId}")
    private String accessKeyId;

    @Value("${prop.accessKeySecret}")
    private String accessKeySecret;

    @Value("${prop.bucketName}")
    private String bucketName;

    @Resource
    private FileInfoService fileService;
    @Resource
    private ChunkService chunkService;
    @Resource
    private UserFileService userFileService;


    @PostMapping("chunk")
    public String uploadChunk(Chunk chunk){
        MultipartFile file = chunk.getFile();

        try{
            byte[] bytes = file.getBytes();
            Path path = Paths.get(generatePath(uploadFolder, chunk));
            Files.write(path, bytes);
            //chunkService.saveChunk(chunk);
            chunkService.rSaveChunk(chunk);
            return "文件上传成功";
        } catch (Exception e){
            return "服务器异常";
        }
    }


    @GetMapping("chunk")
    public Object checkChunk(Chunk chunk, HttpServletResponse rsp){
        //if (chunkService.checkChunk(chunk.getIdentifier(), chunk.getChunkNumber())) {
        if (chunkService.rCheckChunk(chunk.getIdentifier(), chunk.getChunkNumber())) {
            // 如果没上传过就返回304
            rsp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
        return chunk;
    }


    @UserLoginToken
    @PostMapping("mergeFile")
    public String mergeFile(FileInfo fileInfo,@RequestParam Long userId){
        String filename = fileInfo.getFilename(); // 文件名
        String file = uploadFolder + "/" + fileInfo.getIdentifier() + "/" + filename; // 完整路径
        String folder = uploadFolder + "/" + fileInfo.getIdentifier(); //文件夹路径

        System.out.println(filename);
        System.out.println(fileInfo.getIdentifier());

        //从数据库获取文件列表，若存在，直接建立文件用户映射关系，开始合并文件并且上传
        FileInfo one = fileService.findOne(filename, fileInfo.getIdentifier());
        if (one != null){
            userFileService.userAddFile(userId,one);
            System.out.println(fileInfo);
            return "秒传成功";
        } else {
            System.out.println("开始合并文件...");

            mergeAndUploadToOss(endpoint, accessKeyId, accessKeySecret, bucketName, file, folder, filename);
            fileInfo.setLocation(downLoadAddr + filename + "?attname=" + filename);

            System.out.println(downLoadAddr + filename + "?attname=" + filename);

//            merge(file, folder, filename);
//            fileInfo.setLocation(downLoadAddr + "");

            fileInfo.setSize(getPrintSize(fileInfo.getTotalSize()));  //更换文件大小单位
            FileInfo newFile = fileService.addFileInfo(fileInfo);  //添加文件到文件列表
            userFileService.userAddFile(userId,newFile);  //添加用户-文件映射关系
            System.out.println("合并文件完成");
            return "合并成功";
        }

    }

    @UserLoginToken
    @PostMapping("test")
    public Long test(Long userId){
//        userFileService.UserAddFile(fileInfo, userId);
//        ArrayList<Object> objects = new ArrayList<>();
//        objects.add(fileInfo);
//        objects.add(userId);
        return userId;
    }
}
