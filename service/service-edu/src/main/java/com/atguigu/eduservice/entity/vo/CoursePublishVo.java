package com.atguigu.eduservice.entity.vo;

import lombok.Data;

@Data
public class CoursePublishVo {
    //课程id
    private String id;
    //课程题目
    private String title;
    //课程封面
    private String cover;
    //时长
    private Integer lessonNum;
    //一级分类
    private String subjectLevelOne;
    //二级分类
    private String subjectLevelTwo;
    //讲师名称
    private String teacherName;
    //价格
    private String price;//只用于显示
}
