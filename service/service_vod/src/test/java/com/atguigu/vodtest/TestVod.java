package com.atguigu.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

public class TestVod {
    public static void main(String[] args) throws Exception {

        String keyid="LTAI5tPDYUC2SoVEHDBYHKnT";
        String keysecret="lmEVeE2wT9Z3FnrZMklNH0Nc15QwwY";

        //3.音视频上传-本地文件上传
        //视频标题(必选)
        String title = "Video";
        //本地文件上传和文件流上传时，文件名称为上传文件绝对路径，如:/User/sample/文件名称.mp4 (必选)
        //文件名必须包含扩展名
        String fileName = "D:/test.mp4";

        //本地文件上传
        UploadVideoRequest request = new UploadVideoRequest(keyid, keysecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为1M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);

        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }


    //1 根据视频iD获取视频播放凭证
    public static void getPlayAuth() throws Exception {
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tPDYUC2SoVEHDBYHKnT", "lmEVeE2wT9Z3FnrZMklNH0Nc15QwwY");
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        request.setVideoId("1c5fa4d0bc2771eda8ef0764a0ec0102");
        response = client.getAcsResponse(request);
        System.out.println("playAuth:" + response.getPlayAuth());
    }

    //2 根据视频iD获取视频播放地址
    public static void getPlayUrl() throws Exception {
        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tPDYUC2SoVEHDBYHKnT", "lmEVeE2wT9Z3FnrZMklNH0Nc15QwwY");
        //创建获取视频地址request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        //向request对象里面设置视频id
        request.setVideoId("1c5fa4d0bc2771eda8ef0764a0ec0102");
        //调用初始化对象里面的方法，传递request，获取数据
        response = client.getAcsResponse(request);
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息 视频名称
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }

    public static void get(){
        //上传视频
        String accessKeyId = "LTAI5tPDYUC2SoVEHDBYHKnT";
        String accessKeySecret = "lmEVeE2wT9Z3FnrZMklNH0Nc15QwwY";
        String title = "6 - What If I Want to Move Faster";
        String fileName = "C:\\6 - What If I Want to Move Faster";

        //上传视频的方法
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);

        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
}