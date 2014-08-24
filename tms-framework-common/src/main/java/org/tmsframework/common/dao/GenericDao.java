/*
 * Copyright (c) 2006-2011 All Rights Reserved.
 *
 * Author     :zhangsen
 * Version    :1.0
 * Create Date:2011-4-12
 *
 */
package org.tmsframework.common.dao;


import java.io.Serializable;

/**
 * <p>标准Dao接口定义，仅CRU基础方法</p>
 * @author zhangsen
 * @version $Id: GenericDao.java 2011-4-12 下午12:56:30 zhangsen $
 * @param <T> 域对象
 * @param <PK> 域对象主键
 */
public interface GenericDao <T, PK extends Serializable> {
    
    /**
     * 返回域对象的命名空间
     * @return 域对象的命名空间
     */
    public String getSqlMapSpace();

    /**
     * 根据主键编号获取域对象
     * @param id 主键编号 
     * @return 域对象
     */
    public T get(PK id);
    
    
    /**
     * 根据主键编号检查是否存在域对象 
     * @param id 主键编号
     * @return 是否存在
     */
    public boolean exists(PK id);
    
    /**
     * 保存域对象
     * @param object 域对象
     * @return 保存成功的主键
     */
    PK save(T object);
}
