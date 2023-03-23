package com.atguigu.educenter.controller;


import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.utils.R;
import com.atguigu.commonutils.utils.ordervo.UcenterMemberOrder;
import com.atguigu.commonutils.utils.vo.Memeber;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-12
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member) {
        /**
         * member对象封装手机号和密码
         * 调用service方法实现登录
         * 返回token值，使用jwt生成
         */
        String token = memberService.login(member);
        return R.ok().data("token",token);
    }

    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {
        /**
         *  System.out.println(request.getContextPath());
         *  System.out.println(request.getPathInfo());
         *  System.out.println(request.getQueryString());
         *  System.out.println(request.getRequestURI());
         *  System.out.println(request.getRemoteHost());
         *  System.out.println(request.getContextPath());
         */
        //调用jwt工具类的方法。根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

    //根据id查询用户   给评论用的接口
    @GetMapping("getInfoUc/{id}")
    public Memeber getMemberInfo(@PathVariable String id) {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = memberService.getById(id);
        //临时创建用户对象
        Memeber memeber = new Memeber();
        BeanUtils.copyProperties(ucenterMember,memeber);
        return memeber;
    }

    //根据用户id获取用户信息  给订单用的接口
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember member = memberService.getById(id);
        //把member对象里面值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }

    //查询某一天注册人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day) {
        Integer count = memberService.countRegisterDay(day);
        return R.ok().data("countRegister",count);
    }


}

