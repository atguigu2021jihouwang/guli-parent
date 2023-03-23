package com.atguigu.servicebase.exceptionhandler;

import com.atguigu.commonutils.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import com.atguigu.commonutils.utils.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//处理所有异常
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody //为了返回Json数据
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message("执行了全局异常处理..");
    }

    //除数为0执行这个异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理");
    }

    //自定义异常
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        //会将异常日志写到你指定的文件中
        //log.error(e.getMessage());
        log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
