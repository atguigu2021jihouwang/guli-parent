package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.utils.R;
import com.atguigu.commonutils.utils.vo.Memeber;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-16
 */
@RestController
@RequestMapping("/eduservice/comment")
@CrossOrigin
public class EduCommentController {

    @Autowired
    private EduCommentService eduCommentService;

    //注入ucenterClient   微服务调用
    @Autowired
    private UcenterClient ucenterClient;


    //1.分页查询
    @ApiOperation(value = "分页评论列表")
    @GetMapping("{page}/{limit}")
    public R pageListCommentById(@PathVariable Long page, @PathVariable Long limit, String courseId) {
        //创建page对象
        Page<EduComment> pageComment = new Page<>(page, limit);
        Map<String, Object> map = eduCommentService.getCommentList(pageComment, courseId);
        //返回分页所有数据
        return R.ok().data(map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("addComment")
    public R save(@RequestBody EduComment comment, HttpServletRequest request) {
        //查询用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //判断memberId是否为空
        if (StringUtils.isEmpty(memberId)) {
            return R.error().code(20001).message("请登录");
        }
        //用户id
        comment.setMemberId(memberId);
        //获取用户信息
        Memeber memeber = ucenterClient.getMemberInfo(memberId);
        //用户昵称
        comment.setNickname(memeber.getNickname());
        //用户头像
        comment.setAvatar(memeber.getAvatar());

        //保存用户
        eduCommentService.save(comment);

        return R.ok();
    }

}

