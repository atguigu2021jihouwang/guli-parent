package com.atguigu.msmservice.controller;


import com.atguigu.commonutils.utils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone) {
        //1 从redis获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }

        //2 如果redis获取 不到，进行阿里云发送
        //生成随机值，传递阿里云进行发送
        /**
         * 阿里云的验证码需要自己生成,阿里云只是转发
         */
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        //调用service发送短信的方法
        boolean isSend = msmService.send(param, phone);
        if (isSend) {
            //发送成功，把发送成功验证码放到redis里面
            //设置有效时间
            /**
             * phone: key
             * code: value
             * 5: 设置时长(5分钟)
             * TimeUnit.MINUTES: 时长的计量单位
             */
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("短信发送失败");
        }
    }

}
