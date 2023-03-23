package Test;

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

public class test {
    public static void main(String[] args) {

        Map<String, Object> param = new HashMap<>();
        param.put("code", "1234");




        // <accessKeyId>、<accessSecret>上面申请的秘钥
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI5tPDYUC2SoVEHDBYHKnT", "lmEVeE2wT9Z3FnrZMklNH0Nc15QwwY");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "default");
        request.putQueryParameter("PhoneNumbers", "13120582427");
        request.putQueryParameter("SignName", "我的华远在线教育网站");
        request.putQueryParameter("TemplateCode", "SMS_273735156");
        // 模板中的占位符
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
