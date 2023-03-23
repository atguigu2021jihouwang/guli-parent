package com.atguigu.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自定义异常类
@Data
//生成有参数的构造方法
@AllArgsConstructor
//生成无参数的构造方法
@NoArgsConstructor
public class GuliException extends RuntimeException {
    //异常状态码
    private Integer code;
    //异常信息
    private String msg;

    @Override
    public String toString() {
        return "GuliException{" +
                "message=" + this.getMessage() +
                ", code=" + code +
                '}';
    }
}
