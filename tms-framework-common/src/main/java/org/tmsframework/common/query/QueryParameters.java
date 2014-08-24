/*
 * Copyright (c) 2006-2010 All Rights Reserved.
 *
 * Author     :zhangsen
 * Version    :1.0
 * Create Date:2010-12-15
 *
 */
package org.tmsframework.common.query;


import java.util.Map;

/**
 * <p>获取查询参数接口</p>
 * @author zhangsen
 * @version $Id: QueryParameters.java 2010-12-15 上午10:11:52 zhangsen $
 */
public interface QueryParameters {

    /**
     * 获取所有查询参数集
     * @return 所有查询参数集
     */
    public Map<String, String> getParameters();
}
