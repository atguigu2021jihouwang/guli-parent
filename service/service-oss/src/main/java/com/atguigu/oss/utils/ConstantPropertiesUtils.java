package com.atguigu.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//注入到Spring容器
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    //读取到application.properties配置文件内容
    @Value("${aliyun.oss.file.endpoint}") //获取key的值 aliyun.oss.file.endpoint
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    //定义公开静态常量
    public static String END_POIND;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    /**
     * 当项目启动后,上面的属性会加载到Spring容器中;
     * 当属性加载完后,会立即执行接口中的方法
     */

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POIND = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}
