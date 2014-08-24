/*
 * Copyright (c) 2006-2011 All Rights Reserved.
 *
 * Author     :zhangsen
 * Version    :1.0
 * Create Date:2011-4-12
 */
package org.tmsframework.common.dao.ibatis;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.tmsframework.common.dao.GenericDao;

/**
 * <p>通用Dao的Ibatis实现</p>
 * @author zhangsen
 * @version $Id: GenericDaoiBatis.java 2011-4-12 下午01:04:05 zhangsen $
 */
public abstract class GenericDaoiBatis<T, PK extends Serializable> extends BaseDAOIbatis implements
                                                                                  GenericDao<T, PK> {
    /**
    * Logger for this class
    */
    private static final Logger _log = LoggerFactory.getLogger(GenericDaoiBatis.class);

    /**
     * @see org.tmsframework.common.dao.GenericDao#exists(java.io.Serializable)
     */
    public boolean exists(PK id) {
        Object object=get(id);
        return object != null;
    }

    /**
     * @see org.tmsframework.common.dao.GenericDao#get(java.io.Serializable)
     */
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        //判断传入的主键不能为空
        Assert.notNull(id, "获取对象的主键不能为空!");
        return (T) getSqlMapClientTemplate().queryForObject(getSqlMapSpace() + ".queryById", id);
    }

    /**
     * @see org.tmsframework.common.dao.GenericDao#save(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public PK save(T object) {
        //判断保存对象不能为空
        Assert.notNull(object, "保存对象不能为空!");
        return (PK) getSqlMapClientTemplate().insert(getSqlMapSpace() + ".insert", object);
    }

    /**
     * @see org.tmsframework.common.dao.GenericDao#getSqlMapSpace()
     */
    public abstract String getSqlMapSpace();

    /**
     * 利用反射获取对象的主键名
     * @param o 传入的对象
     * @return 对象的主键名
     */
    protected static String getPrimaryKeyFieldName(Object o) {
        Field[] fieldlist = o.getClass().getDeclaredFields();
        String fieldName = null;
        for (Field fld : fieldlist) {
            if (fld.getName().equals("id") || fld.getName().indexOf("Id") > -1
                || fld.getName().equals("version")) {
                fieldName = fld.getName();
                break;
            }
        }
        return fieldName;
    }

    /**
     * 利用反射获取对象的主键类名
     * @param o 传入的对象
     * @return 对象的主键类名
     */
    @SuppressWarnings("unchecked")
    protected static Class getPrimaryKeyFieldType(Object o) {
        Field[] fieldlist = o.getClass().getDeclaredFields();
        Class fieldType = null;
        for (Field fld : fieldlist) {
            if (fld.getName().equals("id") || fld.getName().indexOf("Id") > -1
                || fld.getName().equals("version")) {
                fieldType = fld.getType();
                break;
            }
        }
        return fieldType;
    }

    /**
     * 利用反射获取主键的值
     * @param o 传入的对象
     * @return 对象的主键值
     */
    protected static Object getPrimaryKeyValue(Object o) {
        //使用反射查找名称是否是id或者是Id名字的属性。
        String fieldName = getPrimaryKeyFieldName(o);
        String getterMethod = "get" + Character.toUpperCase(fieldName.charAt(0))
                              + fieldName.substring(1);

        try {
            Method getMethod = o.getClass().getMethod(getterMethod, (Class[]) null);
            return getMethod.invoke(o, (Object[]) null);
        } catch (Exception e) {
            _log.error("无法通过:{} 反射:{}方法", getterMethod, ClassUtils.getShortName(o.getClass()));
        }
        return null;
    }

}
