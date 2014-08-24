package org.tmsframework.demo.query;

import java.util.HashMap;
import java.util.Map;

import org.tmsframework.common.query.QueryBase;

public class UserQuery extends QueryBase {

	private static final long serialVersionUID = 4607791991273674516L;

	public Map<String, String> getParameters() {
		Map<String, String> demo = new HashMap<String, String>();
		demo.put("p1", "aaaa");
		demo.put("v6", "tyuuuu");
		return demo;
	}

}
