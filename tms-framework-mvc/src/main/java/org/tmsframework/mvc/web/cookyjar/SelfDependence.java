package org.tmsframework.mvc.web.cookyjar;

/**
 * ���������ܰ��Լ��־ó�String�����Լ���String�������ֽ�������
 * 
 * @author sam.zhang
 * 
 */
public interface SelfDependence {
	public String lieDown();

	public SelfDependence riseUp(String value);
}
