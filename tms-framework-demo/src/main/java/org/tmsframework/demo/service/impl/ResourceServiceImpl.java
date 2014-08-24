package org.tmsframework.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.tmsframework.demo.domain.Resource;
import org.tmsframework.demo.enums.ResourceType;
import org.tmsframework.demo.service.ResourceService;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

	private static final Map<String, List<Resource>> RESOURCES;
	static {
		RESOURCES = new HashMap<String, List<Resource>>();
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(new Resource("�㽭", "zj", "province", 1));
		RESOURCES.put("province", resources);
	}

	public List<Resource> getResourcesByType(ResourceType type) {
		if (type == null) {
			return null;
		}
		return RESOURCES.get(type.getName());
	}

}
