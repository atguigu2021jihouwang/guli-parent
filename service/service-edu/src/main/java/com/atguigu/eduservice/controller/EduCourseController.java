package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.utils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-02
 */
@Api(description = "课程管理")
//@CrossOrigin
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {

    //访问地址 http://localhost:8001/swagger-ui.html

    @Autowired
    private EduCourseService courseService;

    /**
     * 1.添加课程基本信息
     * 访问地址:http://localhost:8001/eduservice/course/addCourseInfo
     */
    @ApiOperation(value = "添加课程基本信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(
            @ApiParam(name = "courseInfoVo", value = "课程信息对象", required = true)
            @RequestBody CourseInfoVo courseInfoVo) {
        //返回课程信息id
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    /**
     * 2.根据课程id查询课程基本信息
     * 访问地址:http://localhost:8001/eduservice/course/getCourseInfo/{courseId}
     * 列如:http://localhost:8001/eduservice/course/getCourseInfo/1
     */
    @ApiOperation(value = "根据课程id查询课程信息")
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId) {
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo", courseInfoVo);
    }

    /**
     * 3.修改课程信息
     * 访问地址:http://localhost:8001/eduservice/course/updateCourseInfo
     */
    @ApiOperation(value = "根据课程id查询课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(
            @ApiParam(name = "courseInfoVo", value = "课程信息与简介对象", required = true)
            @RequestBody CourseInfoVo courseInfoVo) {
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    /**
     * 4.根据课程id查询课程确认信息
     * 访问地址:http://localhost:8001/eduservice/course/getPublishCourseInfo/{id}
     */
    @ApiOperation(value = "根据课程id查询课程确认信息")
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable String id) {
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse", coursePublishVo);
    }

    /**
     * 5.课程最终发布(修改课程状态)
     */
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        //只是修改状态,因为其他字段值已经添加。这里简单处理
        EduCourse eduCourse = new EduCourse();
        //获取课程的id
        eduCourse.setId(id);
        //设置课程发布状态
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

    /**
     * 6.条件查询带分页
     */
    @ApiOperation(value = "条件查询分页课程列表")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(
            @ApiParam(name = "current", value = "当前页码", required = true) @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
            @ApiParam(name = "courseQuery", value = "查询对象", required = true) @RequestBody(required = false) CourseQuery courseQuery) {

        Page<EduCourse> pageParam = new Page<>(current, limit);
        courseService.pageQuery(pageParam, courseQuery);
        //当前面,显示数据集合
        List<EduCourse> records = pageParam.getRecords();
        //总记录数
        long total = pageParam.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }

    /**
     * 7.删除课程
     * /eduservice/course/{id}
     */
    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){
        boolean result = courseService.removeByCourseId(id);
        if(result){
            return R.ok();
        }else{
            return R.error().message("删除失败");
        }
    }
}

