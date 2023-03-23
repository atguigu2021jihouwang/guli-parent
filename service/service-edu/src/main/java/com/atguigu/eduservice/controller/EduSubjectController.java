package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.utils.R;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-01
 */
@Api(description="课程分类管理")
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

    //访问地址 http://localhost:8001/swagger-ui.html

    @Autowired
    private EduSubjectService subjectService;

    /**
     * 1.添加课程分类
     * 获取上传过来文件，把文件内容读取出来
     * 访问地址:http://localhost:8001/eduservice/subject/addSubject
     */
    @ApiOperation(value = "添加课程分类")
    @PostMapping("addSubject")
    public R addSubject(
            @ApiParam(name = "file", value = "获取上传的文件", required = true)
            MultipartFile file) {
        //上传过来excel文件
        subjectService.saveSubject(file,subjectService);
        return R.ok();
    }

    /**
     * 2.课程分类列表(树形)
     * 访问地址:http://localhost:8001/eduservice/subject/getAllSubject
     */
    @ApiOperation(value = "课程分类列表")
    @GetMapping("getAllSubject")
    public R getAllSubject() {
        //list集合泛型是一级分类
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }

}

