package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-02
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //课程描述注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;
    //注入小节和章节service
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private EduChapterService chapterService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1 向课程表添加课程基本信息(edu_course)这张表添加数据
        //CourseInfoVo对象转换eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        //这里的baseMapper指的是与ServiceImpl对应的Mapper; 这里值的是:EduCourseMapper
        int i = baseMapper.insert(eduCourse); //可以直接用
        if (i <= 0) {
            //手动抛出失败信息
            throw new GuliException(20001, "添加课程信息失败");
        }

        //获取添加之后课程信息id
        String cid = eduCourse.getId();

        //2 向课程简介表添加课程简介(edu_course_description)这张表添加数据
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述id就是课程id
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);
        return cid;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        /**
         * 这2张表用的是同一个id,查看数据库
         */
        //创建CourseInfoVo对象
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        //获取课程基本信息表对象
        EduCourse eduCourse = baseMapper.selectById(courseId);
        //赋值
        BeanUtils.copyProperties(eduCourse, courseInfoVo);
        //获取课程简介表对象
        EduCourseDescription eduCourseDescription = courseDescriptionService.getById(courseId);
        //赋值
        courseInfoVo.setDescription(eduCourseDescription.getDescription());
        //返回重新赋值对象
        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1 修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0) {
            throw new GuliException(20001, "修改课程信息失败");
        }
        //2 修改描述表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    //根据课程id查询课程确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用自己写的mapper
        CoursePublishVo coursePublishVo = baseMapper.getPublishCourseInfo(id);
        return coursePublishVo;
    }

    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        //构建条件
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        //倒序
        queryWrapper.orderByDesc("gmt_create");
        //查询条件为空
        if (courseQuery == null) {
            baseMapper.selectPage(pageParam, queryWrapper);
            return;
        }

        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(title)) {
            //构建条件
            queryWrapper.like("title", title); //这里的字段名: 是指数据库中表的字段名。
        }
        if (!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.ge("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.le("subject_id", subjectId);
        }

        baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public boolean removeByCourseId(String courseId) {

        //1 根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);

        //2 根据课程id删除章节
        chapterService.removeChapterByCourseId(courseId);

        //3 根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        //4 根据课程id删除课程本身
        int result = baseMapper.deleteById(courseId);

        //失败返回
        if (result == 0) {
            throw new GuliException(20001, "删除失败");
        } else {
            return true;
        }
    }

    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo) {

        //2 根据讲师id查询所讲课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件值是否为空，不为空拼接
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) { //一级分类
            wrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())) { //二级分类
            wrapper.eq("subject_id", courseFrontVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())) { //关注度
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) { //最新
            wrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {//价格
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageCourse, wrapper);

        //每页数据list集合
        List<EduCourse> records = pageCourse.getRecords();
        //当前页
        long current = pageCourse.getCurrent();
        //总页数
        long pages = pageCourse.getPages();
        //每页显示记录数
        long size = pageCourse.getSize();
        //总记录数
        long total = pageCourse.getTotal();
        //下一页
        boolean hasNext = pageCourse.hasNext();
        //上一页
        boolean hasPrevious = pageCourse.hasPrevious();

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        //调用自己写的mapper
        CourseWebVo courseWebVo = baseMapper.getBaseCourseInfo(courseId);
        return courseWebVo;
    }
}
