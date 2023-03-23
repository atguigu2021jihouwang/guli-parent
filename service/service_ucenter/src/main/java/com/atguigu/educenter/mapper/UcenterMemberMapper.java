package com.atguigu.educenter.mapper;

import com.atguigu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2023-03-12
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    //查询某一天注册人数
    Integer countRegisterDay(@Param("day") String day);

    /**
     * 注意如果方式的参数是多个参数:
     * 那么在.xml文件中引入参数需要加上注解:@Param("day")
     * 根据注解的值去匹配.xml文件中sql语句里面的参数
     */

}
