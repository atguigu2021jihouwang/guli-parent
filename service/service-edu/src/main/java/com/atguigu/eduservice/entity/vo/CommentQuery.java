package com.atguigu.eduservice.entity.vo;

import lombok.Data;

@Data
public class CommentQuery {

    //课程id
    private String courseId;

    //讲师id
    private String teacherId;

    //用户id
    private String memberId;

    //用户昵称
    private String nickname;

    //用户头像
    private String avatar;

    //评论内容
    private String content;
}
