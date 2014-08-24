/*
 * Copyright (c) 2006-2010 All Rights Reserved.
 *
 * Author     :zhangsen
 * Version    :1.0
 * Create Date:2010-12-15
 *
 */
package org.tmsframework.common.dao.ibatis;


import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.tmsframework.common.query.QueryPage;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * <p>基础DAO的Ibatis实现类</p>
 * @author zhangsen
 * @version $Id: BaseDAO.java 2010-12-15 上午10:11:52 zhangsen $
 */
public abstract class BaseDAOIbatis {
    
    /**
    * Logger for this class
    */
    private static final Logger  logger = LoggerFactory.getLogger(BaseDAOIbatis.class);
    
    @Autowired
    private SqlMapClientTemplate sqlMapClientTemplate;
    

    /**
     * 分页查询封装
     * 
     * @param page          query数据对象，包括分页数据和查询数据
     * @param qTotalCount   count记录总数SQL的sqlid
     * @param qPagination   分页查询SQL的sqlid
     * @return {@link QueryPage} 
     */
    public final QueryPage getPagination(QueryPage page, String qTotalCount,
                                               String qPagination) {
        if (logger.isDebugEnabled()) {
            logger.debug("getPagination(QueryPagination, String, String) - start"); //$NON-NLS-1$
        }

        if (!(page instanceof QueryPage)) {
            throw new IllegalArgumentException("'page' argument is unsupport class type, "
                                               + "it must be " + QueryPage.class.getName()
                                               + " or subclass");
        }
        int totalCount = (Integer) this.getSqlMapClientTemplate().queryForObject(qTotalCount, page);
        page.setTotalItem(totalCount);
        if (totalCount > 0) {
            page.setItems(this.getSqlMapClientTemplate().queryForList(qPagination, page));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getPagination(QueryPagination, String, String) - end"); //$NON-NLS-1$
        }
        return page;
    }

    /**
     * 实际执行批量操作的方法,代理执行插入、更新、删除等操作
     * @param statementName 语句
     * @param parameterList 参数列表
     * @return 批量执行数
     */
    @SuppressWarnings("unchecked")
    private int executeBatchOperation(final String statementName, final List parameterList,
                                      final String flag) {
        if (logger.isDebugEnabled()) {
            logger.debug("executeBatchOperation(String, List, String) - start"); //$NON-NLS-1$
        }

        Long exectuteSucValue = null;
        exectuteSucValue = (Long) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                if (logger.isDebugEnabled()) {
                    logger.debug("$SqlMapClientCallback.doInSqlMapClient(SqlMapExecutor) - start"); //$NON-NLS-1$
                }

                Long states = Long.valueOf(0);
                try {
                    executor.startBatch();
                    for (int i = 0; i < parameterList.size(); i++) {
                        if (flag.equals("update")) {
                            executor.update(statementName, parameterList.get(i));
                        } else if (flag.equals("insert")) {
                            executor.insert(statementName, parameterList.get(i));
                        } else if (flag.equals("delete")) {
                            executor.delete(statementName, parameterList.get(i));
                        }
                    }

                    executor.executeBatch();
                } catch (Exception e) {
                    if (logger.isErrorEnabled()) {
                        logger.error("执行批处理操作时出错！" + e);
                    }
                    states = Long.valueOf(-1);
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("$SqlMapClientCallback.doInSqlMapClient(SqlMapExecutor) - end"); //$NON-NLS-1$
                }
                return states;

            }
        });
        if (exectuteSucValue.intValue() == -1) {
            throw new RuntimeException();
        }
        int returnint = parameterList.size();
        if (logger.isDebugEnabled()) {
            logger.debug("executeBatchOperation(String, List, String) - end"); //$NON-NLS-1$
        }
        return returnint;
    }

    /**
     * 批量更新
     * @param statementName 语句
     * @param parameterList 参数列表
     * @return 批量执行数
     */
    @SuppressWarnings("unchecked")
    protected int executeBatchUpdate(final String statementName, List parameterList) {
        return this.executeBatchOperation(statementName, parameterList, "update");
    }

    /**
     * 批量添加
     * @param statementName 语句
     * @param parameterList 参数列表
     * @return 批量执行数
     */
    @SuppressWarnings("unchecked")
    protected int exectuteBatchInsert(final String statementName, List parameterList) {
        return this.executeBatchOperation(statementName, parameterList, "insert");
    }

    /**
     * 批量删除
     * @param statementName 语句
     * @param parameterList 参数列表
     * @return 批量执行数
     */
    @SuppressWarnings("unchecked")
    protected int executeBatchDelete(final String statementName, List parameterList) {
        return this.executeBatchOperation(statementName, parameterList, "delete");
    }

    /**
     * @return the sqlMapClientTemplate
     */
    public SqlMapClientTemplate getSqlMapClientTemplate() {
        return sqlMapClientTemplate;
    }

    /**
     * @param sqlMapClientTemplate the sqlMapClientTemplate to set
     */
    public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
        this.sqlMapClientTemplate = sqlMapClientTemplate;
    }
}