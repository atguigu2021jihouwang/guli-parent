package com.atguigu.oss.controller;

import com.atguigu.commonutils.utils.R;
import com.atguigu.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description="OSS管理")
@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {

    //http://localhost:8002/swagger-ui.html 访问地址
    @Autowired
    private OssService ossService;

    @ApiOperation(value = "头像上传") //结合swagger-ui 使用
    @PostMapping
    public R uploadOssFile(
            @ApiParam(name = "file", value = "上传的头像文件", required = true) MultipartFile file) {
        //获取上传文件  MultipartFile
        //返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }
}
