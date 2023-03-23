package com.atguigu.eduservice.client;

import com.atguigu.commonutils.utils.R;
import com.atguigu.commonutils.utils.vo.Memeber;
import org.springframework.stereotype.Component;

@Component
public class UcenterFileDegradeFeignClient implements UcenterClient{

    @Override
    public Memeber getMemberInfo(String id) {
        return null;
    }
}
