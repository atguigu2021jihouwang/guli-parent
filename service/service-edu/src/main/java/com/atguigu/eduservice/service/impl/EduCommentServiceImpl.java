package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.mapper.EduCommentMapper;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-16
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Override
    public Map<String, Object> getCommentList(Page<EduComment> pageComment, String courseId) {

        //构建查询条件
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.orderByDesc("gmt_create");
        baseMapper.selectPage(pageComment,wrapper);

        //每页数据list集合
        List<EduComment> records = pageComment.getRecords();
        //当前页
        long current = pageComment.getCurrent();
        //总页数
        long pages = pageComment.getPages();
        //每页显示记录数
        long size = pageComment.getSize();
        //总记录数
        long total = pageComment.getTotal();
        //下一页
        boolean hasNext = pageComment.hasNext();
        //上一页
        boolean hasPrevious = pageComment.hasPrevious();

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }
}
