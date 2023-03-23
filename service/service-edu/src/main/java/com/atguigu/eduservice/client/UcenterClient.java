package com.atguigu.eduservice.client;

import com.atguigu.commonutils.utils.vo.Memeber;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-ucenter",fallback = UcenterFileDegradeFeignClient.class)
@Component
public interface UcenterClient {

    @GetMapping("/educenter/member/getInfoUc/{id}")
    public Memeber getMemberInfo(@PathVariable("id") String id);

}
