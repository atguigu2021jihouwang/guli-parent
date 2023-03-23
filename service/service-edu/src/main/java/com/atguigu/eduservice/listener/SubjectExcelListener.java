package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Map;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    /**
     * 因为SubjectExcelListener不能交给spring进行管理，需要自己new，不能注入其他对象
     * 比如:EduSubjectService等等
     * 不能实现相关操作,需要自己手动来添加
     */
    //解决办法
    public EduSubjectService subjectService;
    public SubjectExcelListener(){}
    public SubjectExcelListener(EduSubjectService subjectService) {
        //手动将subjectService注入
        this.subjectService = subjectService;
    }

    //一行一行读取excel内容
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        //subjectData: 获取Excel文件中的源数据对象
        //判断文件是否为空
        if(subjectData == null) {
            throw new GuliException(20001,"文件数据不能为空");
        }

        //一行一行读取，每次读取有两个值，第一个值一级分类，第二个值二级分类
        /**
         * subjectService: 操作数据库的对象
         * subjectData.getOneSubjectName()
         */
        System.out.println(subjectData.getOneSubjectName());
        //判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());
        //没有相同一级分类,进行添加
        if(existOneSubject == null) {
            //添加一级分类
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName());//一级分类名称
            subjectService.save(existOneSubject);
        }

        //获取一级分类id值
        String pid = existOneSubject.getId();

        //判断二级分类是否重复
        EduSubject existTwoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(), pid);
        if(existTwoSubject == null) {
            //添加二级分类
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());//二级分类名称
            subjectService.save(existTwoSubject);
        }
    }

    //读取表头内容
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {

    }

    //读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService, String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject twoSubject = subjectService.getOne(wrapper);
        return twoSubject;
    }
}
