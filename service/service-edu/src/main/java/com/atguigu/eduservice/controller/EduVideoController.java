package com.atguigu.eduservice.controller;


import com.alibaba.excel.util.StringUtils;
import com.atguigu.commonutils.utils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-02
 */
@Api(description = "小节管理")
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    //注入vodClient   微服务调用
    @Autowired
    private VodClient vodClient;

    /**
     * 1.添加小节
     * 访问地址:http://localhost:8001/eduservice/video/addVideo
     */
    @ApiOperation(value = "添加小节")
    @PostMapping("addVideo")
    public R addVideo(
            @ApiParam(name = "addVideo", value = "小节对象", required = true)
            @RequestBody EduVideo eduVideo) {
        videoService.save(eduVideo);
        return R.ok();
    }

    /**
     * 2.删除小节
     * 访问地址:http://localhost:8001/eduservice/video/1
     */
    // TODO 后面这个方法需要完善：删除小节时候，同时把里面视频删除
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id) {
        //根据小节id获取视频id，调用方法实现视频删除
        EduVideo eduVideo = videoService.getById(id);
        //获取视频id
        String videoSourceId = eduVideo.getVideoSourceId();
        //判断小节里面是否有视频id
        if (!StringUtils.isEmpty(videoSourceId)) {
            //根据视频id，远程调用实现视频删除
            R result = vodClient.removeAlyVideo(videoSourceId);
            if (result.getCode() == 20001) {
                throw new GuliException(20001, "删除视频失败，熔断器...");
            }
        }

        //删除小节
        videoService.removeById(id);
        return R.ok();
    }

    /**
     * 3.修改小节
     * 访问地址:http://localhost:8001/eduservice/video/updateVideo
     */
    @ApiOperation(value = "修改小节")
    @PostMapping("updateVideo")
    public R updateVideo(
            @ApiParam(name = "eduVideo", value = "修改小节对象", required = true)
            @RequestBody EduVideo eduVideo) {
        videoService.updateById(eduVideo);
        return R.ok();
    }

    /**
     * 4.根据id查询小节
     * 访问地址:http://localhost:8001/eduservice/video/getVideoInfo/{videoId}
     */
    @ApiOperation(value = "根据id查询小节")
    @GetMapping("getVideoInfo/{videoId}")
    public R getChapterInfo(
            @ApiParam(name = "videoId", value = "小节id", required = true)
            @PathVariable String videoId) {
        EduVideo eduVideo = videoService.getById(videoId);
        return R.ok().data("video", eduVideo);
    }
}

