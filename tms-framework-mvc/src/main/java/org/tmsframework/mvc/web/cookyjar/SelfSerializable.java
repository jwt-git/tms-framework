package org.tmsframework.mvc.web.cookyjar;

/**
 * 实现自身的序列化与反序列化
 * 
 * @author sam.zhang
 * 
 */
public interface SelfSerializable {
	
	public String lieDown();

	public SelfSerializable riseUp(String value);
}
