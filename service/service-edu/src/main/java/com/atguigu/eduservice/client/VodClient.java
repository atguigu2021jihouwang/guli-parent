package com.atguigu.eduservice.client;

import com.atguigu.commonutils.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//调用的服务名称

/**
 * name = "service-vod" 被需要调用的模块名称叫: service-vod
 */
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
@Component
public interface VodClient {

    //定义调用的方法路径
    //根据视频id删除阿里云视频
    //@PathVariable注解一定要指定参数名称，否则出错
    //路径需要写成完整的路径
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id);

    //定义调用删除多个视频的方法
    //删除多个阿里云视频的方法
    //参数多个视频id  List videoIdList
    @DeleteMapping("/eduvod/video/deletebatch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
