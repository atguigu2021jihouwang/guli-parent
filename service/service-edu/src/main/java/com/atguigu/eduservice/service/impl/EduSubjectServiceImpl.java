package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-01
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {
        //文件输入流
        try {
            InputStream in = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(in, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");
        /**
         * 因为当前没有创建EduSubjectMapper的实例,所以不能被Spring管理,
         * 但ServiceImpl接口中封装了baseMapper,并被Spring管理,这里可以直接使用
         */
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //查询二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        //查询parent_id字段不等于0
        wrapperTwo.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //创建一级分类集合
        List<OneSubject> finalSubjectList = new ArrayList<>();
        //封装一级分类
        for (int i = 0; i < oneSubjectList.size(); i++) {
            //获取每个一级分类eduSubject对象
            EduSubject oneEduSubject = oneSubjectList.get(i);
            //创建OneSubject对象
            OneSubject oneSubject = new OneSubject();
            //oneSubject.setId(eduSubject.getId());
            //oneSubject.setTitle(eduSubject.getTitle());
            /**
             * 将一个对象赋值到另一个对象上
             * 相当于: oneSubject.setId(eduSubject.getId());
             *        oneSubject.setTitle(eduSubject.getTitle());
             *  先赋值(set),再取值(get)
             */
            //简写形式
            BeanUtils.copyProperties(oneEduSubject, oneSubject);
            //将一级分类对象添加到集合
            finalSubjectList.add(oneSubject);

            /**
             * 在一级分类循环遍历查询所有的二级分类
             */
            //创建list集合封装每个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();

            for (int j = 0; j < twoSubjectList.size(); j++) {
                //获取每个二级分类eduSubject对象
                EduSubject twoEduSubject = twoSubjectList.get(j);
                //判断二级分类的parent_id和一级分类的id是否一样
                if (twoEduSubject.getParentId().equals(oneEduSubject.getId())) {
                    //创建TwoSubject对象
                    TwoSubject twoSubject = new TwoSubject();
                    //简写形式
                    BeanUtils.copyProperties(twoEduSubject, twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            //获取一级分类下的二级分类集合
            oneSubject.setChildren(twoFinalSubjectList);
        }
        return finalSubjectList;
    }
}
