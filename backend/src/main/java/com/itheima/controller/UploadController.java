package com.itheima.controller;

import com.itheima.utils.AliyunOSSOperator;
import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
public class UploadController {

    /**
     * 本地磁盘存储方案（不推荐）
     * @param name
     * @param age
     * @param file
     * @return
     * @throws IOException
     */
  /*  @PostMapping("/upload")
    public Result upload(String name, Integer age, MultipartFile file) throws IOException {
        log.info("接收参数:{},{},{}",name,age,file);
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();

        //新的文件名
        String extension= originalFilename.substring(originalFilename.lastIndexOf("."));//截取文件名后缀
        String newFileName= UUID.randomUUID().toString()+extension;

        //保存文件
        file.transferTo(new File("/Users/mac/Pictures/"+newFileName));
        return Result.success();
    }*/

    /**
     * 阿里云OSS存储方案
     * @param name
     * @param age
     * @param file
     * @return
     * @throws IOException
     */

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传：{}",file.getOriginalFilename());
        //将文件交给 OSS 存储管理
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        log.info("文件上传到 OSS，url：{}",url);
        return Result.success(url);
    }

}
