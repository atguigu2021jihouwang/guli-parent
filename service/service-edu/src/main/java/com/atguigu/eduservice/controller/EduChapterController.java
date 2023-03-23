package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.utils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-02
 */
@Api(description = "章节管理")
@RestController
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {

    //访问地址 http://localhost:8001/swagger-ui.html

    @Autowired
    private EduChapterService chapterService;

    /**
     * 1.课程大纲列表,根据课程id进行查询
     * 访问地址:http://localhost:8001/eduservice/chapter/getChapterVideo/{courseId}
     * 列如:http://localhost:8001/eduservice/chapter/getChapterVideo/1
     */
    @ApiOperation(value = "根据课程id查询,课程大纲列表")
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId) {
        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("allChapterVideo",list);
    }

    /**
     * 2.添加章节
     * 访问地址:http://localhost:8001/eduservice/chapter/addChapter
     */
    @ApiOperation(value = "添加章节")
    @PostMapping("addChapter")
    public R addChapter(
            @ApiParam(name = "eduChapter", value = "添加的章节对象", required = true)
            @RequestBody EduChapter eduChapter) {
        //保存
        boolean save = chapterService.save(eduChapter);
        if(save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 3.根据id查询章节
     * 访问地址:http://localhost:8001/eduservice/chapter/getChapterInfo/{chapterId}
     * 列如:http://localhost:8001/eduservice/chapter/getChapterInfo/1
     */
    @ApiOperation(value = "根据课程id进行查询章节")
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(
            @ApiParam(name = "chapterId", value = "章节id", required = true)
            @PathVariable String chapterId) {
        EduChapter eduChapter = chapterService.getById(chapterId);
        return R.ok().data("chapter",eduChapter);
    }

    /**
     * 4.修改章节
     * 访问地址:http://localhost:8001/eduservice/chapter/updateChapter
     */
    @ApiOperation(value = "修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(
            @ApiParam(name = "eduChapter", value = "修改章节对象", required = true)
            @RequestBody EduChapter eduChapter) {
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    /**
     * 5.根据id删除章节
     * 访问地址:http://localhost:8001/eduservice/chapter/{chapterId}
     */
    @ApiOperation(value = "根据课程id,删除章节")
    @DeleteMapping("{chapterId}")
    public R deleteChapter(
            @ApiParam(name = "chapterId", value = "课程id", required = true)
            @PathVariable String chapterId) {
        boolean flag = chapterService.deleteChapter(chapterId);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

