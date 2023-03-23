package com.atguigu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    @Override
    public boolean send(Map<String, Object> param, String phone) {

        if (StringUtils.isEmpty(phone)) return false;

        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI5tPDYUC2SoVEHDBYHKnT", "lmEVeE2wT9Z3FnrZMklNH0Nc15QwwY");
        IAcsClient client = new DefaultAcsClient(profile);
        /**
         * 设置相关固定的参数
         */
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        //提交方式
        request.setMethod(MethodType.POST);
        //请求阿里云中的哪个模块
        request.setDomain("dysmsapi.aliyuncs.com");
        //版本号
        request.setVersion("2017-05-25");
        //请求模块中的哪个方法
        request.setAction("SendSms");

        /**
         * 设置发送相关的参数
         */
        request.putQueryParameter("RegionId", "default");
        //手机号
        request.putQueryParameter("PhoneNumbers", phone);
        //申请阿里云 签名名称  我的华远在线教育网站
        request.putQueryParameter("SignName", "我的华远在线教育网站");
        //申请阿里云 模板code
        request.putQueryParameter("TemplateCode", "SMS_273735156");
        //验证码数据，转换json数据传递
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));
        try {
            //最终发送
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
