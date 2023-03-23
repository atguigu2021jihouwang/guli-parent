package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-16
 */
public interface EduCommentService extends IService<EduComment> {

    Map<String, Object> getCommentList(Page<EduComment> pageCourse, String courseId);



}
