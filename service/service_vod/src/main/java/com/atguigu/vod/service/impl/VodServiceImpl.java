package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.UploadVideo;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitVodCilent;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;


import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class VodServiceImpl implements VodService {

    @Override
    public String uploadVideoAly(MultipartFile file) {
        UploadStreamRequest request = null;
        try {
            /**
             * title : 上传之后显示的名称
             * fileName: 上传文件原始名称
             * inputStream : 上传文件输入流
             */
            String fileName = file.getOriginalFilename();
            //title不让它带后缀名，个人建议
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //一般我们都需要保存上传视频的ID
        String videoId = null;
        //创建实现上传的对象
        UploadVideo uploader = new UploadVideoImpl();
        //上传动作，到这一步，已经上传成功
        UploadStreamResponse response = uploader.uploadStream(request);
        //下面是打印信息，用于测试，并且保存下videoId，
        //以便后面通过videoId获取到视频的播放地址或播放凭证
        System.out.println("RequestId=" + response.getRequestId());
        if (response.isSuccess()) {
            videoId = response.getVideoId();
            System.out.println("VideoId=" + response.getVideoId());
        } else {
            System.out.println("videoId=" + response.getVideoId());
            System.out.println("ErrorCode=" + response.getCode());
            System.out.println("ErrorMessage=" + response.getMessage());
        }
        return videoId;
    }

    @Override
    public void removeMoreAlyVideo(List<String> videoIdList) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //videoIdList值转换成 1,2,3
            String videoIds = StringUtils.join(videoIdList.toArray(), ",");
            //向request设置视频id
            request.setVideoIds(videoIds);
            //调用初始化对象的方法实现删除  org.apache.commons.lang.StringUtils;
            client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }
}
