/*
 * Copyright (c) 2006-2011 All Rights Reserved.
 *
 * Author     :zhangsen
 * Version    :1.0
 * Create Date:2011-1-6
 *
 */
package org.tms.framework.common.query;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>查询分页</p>
 * @author zhangsen
 * @version $Id: QueryPage.java 2011-1-6 上午10:18:10 zhangsen $
 */
public class QueryPage extends QueryBase implements QueryGetParameters {
    
    /**
    * Logger for this class
    */
    private static final Logger logger           = LoggerFactory.getLogger(QueryPage.class);
    
    /**
     * 序列值 
     */
    private static final long serialVersionUID = -4734192696485746607L;
    
    /** 获取参数对象  */
    Object obj;
    
    /** 是否需要分页  */
    String queryPageClose;
    
    /** 分页数据 */
    List<?> items;
    
    /** 不带参数构造函数  */
    public QueryPage(){
    }
    
    
    /** 带参数构造函数  */
    public QueryPage(Object obj){
        this.obj=obj;
    }
    

    /**
     * @see com.hundsun.network.common.query.QueryGetParameters#getParameters()
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getParameters() {
        Assert.notNull(obj);
        Class clazz = obj.getClass();
        HashMap<String, String> resMap = new HashMap<String, String>();
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            getClass(clazz, map, obj);
            resMap =convertHashMap(map);
        } catch (Exception e) {     
            if(logger.isErrorEnabled()){
                logger.error("{}",e);
            }
        }
        return resMap;
    }
    
    
    @SuppressWarnings("unchecked")
    private static void getClass(Class clazz, HashMap map, Object obj)
            throws Exception {
        if (clazz.getSimpleName().equals("Object")) {
            return;
        }
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);              
                String name = fields[i].getName();
                if("serialVersionUID".equals(name)){
                    continue;
                }
                Object value = fields[i].get(obj);
                map.put(name, value);

            }
        }
        Class superClzz = clazz.getSuperclass();
        getClass(superClzz, map, obj);
    }

    /**
     * 
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private static HashMap<String,String> convertHashMap(HashMap map)
            throws Exception {

        HashMap<String,String> newMap = new HashMap<String,String>();
        Set keys = map.keySet();
        java.util.Iterator it = keys.iterator();
        while (it.hasNext()) {
            Object key = it.next();
            convertToString(map.get(key), newMap, key);
        }

        return newMap;
    }

    /**
     * 
     * @param value
     * @param newMap
     * @param key
     */
    @SuppressWarnings("unchecked")
    private static void convertToString(Object value, HashMap newMap, Object key) {
        if (value != null) {
            Class clazz = value.getClass();
            if (isBaseType(clazz)) {
                newMap.put(key, value.toString());
            } else if (clazz == String.class) {
                newMap.put(key, value.toString());
            } else if (clazz == Date.class) {
                Date date = (Date) value;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                newMap.put(key, sdf.format(date));
            } else if (clazz == Timestamp.class) {
                Timestamp timestamp = (Timestamp) value;
                long times = timestamp.getTime();
                Date date = new Date(times);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                newMap.put(key, sdf.format(date));
            } else if (clazz == java.sql.Date.class) {
                java.sql.Date sqlDate = (java.sql.Date) value;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                newMap.put(key, sdf.format(sqlDate));
            } else {
                newMap.put(key, value);
            }
        }

    }

    /**
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    private static boolean isBaseType(Class clazz) {
        boolean isBaseType=false;
        
        if (clazz == Integer.class) {
            isBaseType=true;
        }
        if (clazz == Long.class) {
            isBaseType=true;
        }
        if (clazz == Double.class) {
            isBaseType=true;
        }
        if (clazz == Byte.class) {
            isBaseType=true;
        }
        if (clazz == Float.class) {
            isBaseType=true;
        }
        if (clazz == Short.class) {
            isBaseType=true;
        }
        if (clazz == Boolean.class) {
            isBaseType=true;
        }
        return isBaseType;
    }


    /**
     * @return the items
     */
    public List<?> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<?> items) {
        this.items = items;
    }


    /**
     * @return the obj
     */
    public Object getObj() {
        return obj;
    }


    /**
     * @param obj the obj to set
     */
    public void setObj(Object obj) {
        this.obj = obj;
    }


    /**
     * @return the queryPageClose
     */
    public String getQueryPageClose() {
        return queryPageClose;
    }


    /**
     * @param queryPageClose the queryPageClose to set
     */
    public void setQueryPageClose() {
        this.queryPageClose = "TRUE";
    }
    
    
}
