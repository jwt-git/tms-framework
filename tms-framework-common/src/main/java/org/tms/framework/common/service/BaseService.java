/*
 * Copyright (c) 2006-2010 All Rights Reserved.
 *
 * Author     :zhangsen
 * Version    :1.0
 * Create Date:2010-12-15
 *
 */
package org.tms.framework.common.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * <p>基础Service</p>
 * @author shencb
 * @version $Id: BaseService.java 2010-12-15 上午10:11:52 shencb $
 */
public abstract class BaseService {
    /**
    * Logger for this class
    */
    protected static final Logger   logger = LoggerFactory.getLogger(BaseService.class);
	
    /**
     * 事务模板
     */
    protected TransactionTemplate transactionTemplate;

    /**
     * @return the transactionTemplate
     */
    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    /**
     * @param transactionTemplate the transactionTemplate to set
     */
    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
