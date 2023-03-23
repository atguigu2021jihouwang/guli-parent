package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-02-24
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    //1 分页查询讲师的方法
    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher) {
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //降序
        wrapper.orderByDesc("id");
        //把分页数据封装到pageTeacher对象
        baseMapper.selectPage(pageTeacher,wrapper);

        //每页数据list集合
        List<EduTeacher> records = pageTeacher.getRecords();
        //当前页
        long current = pageTeacher.getCurrent();
        //总页数
        long pages = pageTeacher.getPages();
        //每页显示记录数
        long size = pageTeacher.getSize();
        //总记录数
        long total = pageTeacher.getTotal();
        //下一页
        boolean hasNext = pageTeacher.hasNext();
        //上一页
        boolean hasPrevious = pageTeacher.hasPrevious();

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
