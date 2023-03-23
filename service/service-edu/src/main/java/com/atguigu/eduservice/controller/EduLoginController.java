package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(description="用户管理")
@RestController
@RequestMapping("/eduservice/user")
//解决跨域问题
//@CrossOrigin
public class EduLoginController {

    //login
    @ApiOperation(value = "登录")
    @PostMapping("login")
    public R login() {
        return R.ok().data("token","admin");
    }

    //info
    @ApiOperation(value = "用户信息")
    @GetMapping("info")
    public R info() {
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
