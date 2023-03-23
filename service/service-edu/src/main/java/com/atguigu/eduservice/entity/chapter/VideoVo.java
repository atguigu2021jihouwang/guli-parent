package com.atguigu.eduservice.entity.chapter;

import lombok.Data;

//小节
@Data
public class VideoVo {
    private String id;
    private String title;
    private String videoSourceId; //视频id
}
