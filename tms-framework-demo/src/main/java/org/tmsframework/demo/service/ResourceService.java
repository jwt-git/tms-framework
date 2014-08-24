package org.tmsframework.demo.service;

import java.util.List;

import org.tmsframework.demo.domain.Resource;
import org.tmsframework.demo.enums.ResourceType;


/**
 * 资源Service接口
 * 
 * @author zhengdd
 * @version $Id: ResourceService.java,v 0.1 2010-6-24 上午11:09:15 zhengdd Exp $
 */
public interface ResourceService {

    /**
     * 根据资源类型获取资源列表
     * 
     * @param province
     * @return List<Resource>
     */
    public List<Resource> getResourcesByType(ResourceType province);

}