/**
 * 
 */
package org.tmsframework.mvc.common;

import java.io.Serializable;

import org.tmsframework.io.cookie.SelfUtil;
import org.tmsframework.mvc.web.cookyjar.SelfSerializable;

/**
 * 面包屑
 * 		用于监视用户是否已经关闭浏览器，如果关闭浏览器的话就清除此面包屑，下次进入时则需要再次登录
 * 		模拟session实现	
 * 
 * @author zhangsen
 *
 */
public class Crumbs implements Serializable, SelfSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -693690748726731186L;
	
	
	public static String CRUMBS_CONTENT = "crumbs"; 
	
	
	@Override
	public String lieDown() {
		return SelfUtil.format(Crumbs.CRUMBS_CONTENT);
	}

	@Override
	public SelfSerializable riseUp(String arg0) {
		return this;
	}

}
