package com.atguigu.servicebase.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * myBatisPlus中处理自动填充
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * @param metaObject 源数据(数据库表的源数据)
     * 这里的字段: 是指创建实体类中的属性字段
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }
}
