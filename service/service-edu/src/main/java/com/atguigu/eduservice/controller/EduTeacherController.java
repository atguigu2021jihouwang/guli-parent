package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.utils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-02-24
 */
//结合swagger-ui 使用
@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher") //这里/必须加上
//解决跨域问题
@CrossOrigin
public class EduTeacherController {

    //http://localhost:8001/swagger-ui.html 访问地址

    //http://localhost:8222/eduservice/teacher/findAll

    /**
     * 只需要将service注入进来,如mapper等等已经封装好了
     */
    @Autowired
    private EduTeacherService teacherService;

    /**
     * 1 查询讲师表所有数据
     * rest风格
     * 访问地址:http://localhost:8001/eduservice/teacher/findAll
     */
    @ApiOperation(value = "所有讲师列表") //结合swagger-ui 使用
    @GetMapping("findAll") //这里加不加/都可以
    public R findAllTeacher() {
        try {
           // int a = 10/0;
        } catch (Exception e) {
            throw new GuliException(20001,"执行了自定义异常处理。。。");
        }


        //调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    /**
     * 2 逻辑删除讲师的方法
     * rest风格
     * 访问地址:http://localhost:8001/eduservice/teacher/id
     *
     * {id}:这种传参方式为:从路径中得到 列如:http://localhost:8001/eduservice/teacher/1
     * @PathVariable: 注解 获取路径中带过来的参数
     * @ApiParam: 结合swagger-ui 使用
     */
    @ApiOperation(value = "根据ID删除讲师") //结合swagger-ui 使用
    @DeleteMapping("{id}")
    public R removeTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {
        boolean flag = teacherService.removeById(id);
        if(flag) {
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 3 分页查询讲师的方法
     * rest风格
     * 访问地址:http://localhost:8001/eduservice/teacher/pageTeacher/{current}/{limit}
     * {current}:当前页; {limit}:每页记录数 (这种传参方式为:从路径中得到)
     * 列如:http://localhost:8001/eduservice/teacher/pageTeacher/1/2
     */
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(
            @ApiParam(name = "current", value = "当前页码", required = true) @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit) {
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        /**
         * 调用方法实现分页
         * 调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
         * 通过page对象获取分页数据
         * //当前页
         * System.out.println("1:" + page.getCurrent());
         * //每页数据list集合
         * System.out.println("2:" + page.getRecords());
         *  //每页显示记录数
         *  System.out.println("3:" + page.getSize());
         *  //总记录数
         *  System.out.println("4:" + page.getTotal());
         *  //总页数
         *  System.out.println("5:" + page.getPages());
         *  //下一页
         *  System.out.println("6:" + page.hasNext());
         *  //上一页
         *  System.out.println("7:" + page.hasPrevious());
         */
        teacherService.page(pageTeacher,null);
        //数据list集合(当前面,显示数据的集合)
        List<EduTeacher> records = pageTeacher.getRecords();
        //总记录数
        long total = pageTeacher.getTotal();

        //第一种方式
        //return R.ok().data("total",total).data("rows",records);

        //第二种方式
        Map map = new HashMap();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);
    }

    /**
     * 4条件查询带分页的方法
     * @ResponseBody:用于返回Json数据的注解
     * @RequestBody:使用Json数据传递数据,把Json数据封装到对象里面
     * 传递参数:
     *  方法一:
     *      直接传递(没有前面的注解);
     *      如:TeacherQuery teacherQuery 只能适用与get提交(前端数据可以直接映射到teacherQuery对象里面)
     *  方法二:
     *      加上 @RequestBody(required = false)注解
     *      表示:前端请求服务器传递的数据形式为Json,Spring会将Json数据转化封装到对象里面,但请求方式必须是Post请求;
     *      (required = false)表示:这个对象可以为空
     *  访问地址:http://localhost:8001/eduservice/teacher/pageTeacherCondition/{current}/{limit}
     *  列如:http://localhost:8001/eduservice/teacher/pageTeacherCondition/1/2
     */
    @ApiOperation(value = "条件查询分页讲师列表")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @ApiParam(name = "current", value = "当前页码", required = true) @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
            @ApiParam(name = "teacherQuery", value = "查询对象", required = true) @RequestBody(required = false) TeacherQuery teacherQuery ) {

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        //多条件组合查询(mybatis学过 动态sql)
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        //判断条件值是否为空，如果不为空拼接条件
        if(!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name",name); //这里的字段名: 是指数据库中表的字段名。
        }
        if(!StringUtils.isEmpty(level)) {
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create",end);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        //带条件的分页查询
        teacherService.page(pageTeacher,wrapper);

        //数据list集合(当前面,显示数据的集合)
        List<EduTeacher> records = pageTeacher.getRecords();
        //总记录数
        long total = pageTeacher.getTotal();

        //第一种方式
        return R.ok().data("total",total).data("rows",records);
    }

    /**
     * 5添加讲师接口的方法
     * http://localhost:8001/eduservice/teacher/addTeacher
     */
    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(
            @ApiParam(name = "eduTeacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        if(save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 6根据讲师id进行查询
     * 访问地址:http://localhost:8001/eduservice/teacher/getTeacher/{id}
     * 列如:http://localhost:8001/eduservice/teacher/getTeacher/1
     */
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    /**
     * 7讲师修改功能
     */
    @ApiOperation(value = "修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(
            @ApiParam(name = "eduTeacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher eduTeacher) {
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

}

