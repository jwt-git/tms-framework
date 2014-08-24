package org.tmsframework.demo.domain;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.tmsframework.mvc.web.cookyjar.SelfSerializable;

/**
 * SNA架构中,此对象表示在cookie中持久化的会员,此对象只包含了最常使用的会员数据,并且在结构上一个扁平的简单对象
 * 
 * @author fish
 * 
 */
public class UserAgent implements SelfSerializable{

//	public static final String UserAgentTag = "userAgent";

	private String realName;// 姓名
	private String province;// User.NativePlace.province
	private String city;// //User.NativePlace.city
	private Date loginTime;// 登录时间

	public UserAgent() {
		super();
	}

	public UserAgent(User u) {
		super();
		this.setRealName(u.getRealName());
		this.setProvince(u.getNativePlace().getProvince());
		this.setCity(u.getNativePlace().getCity());
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	private static final char Seq = '|';

	/**
	 * 把自己持久化成string串
	 */
	public String lieDown() {
		// 注意,此demo假设用户名,地区的字符串中不会出现 | 这个字符,所有简单的使用作为分隔符
		StringBuilder sb = new StringBuilder();
		sb.append(this.realName).append(Seq);
		sb.append(this.province).append(Seq);
		sb.append(this.city).append(Seq);
		sb.append(Long.toString(this.loginTime.getTime(), 32));// 32进制表示long,长度比较短
		return sb.toString();
	}

	public SelfSerializable riseUp(String value) {
		String[] values = StringUtils.split(value, Seq);
		if (values == null || values.length != 4) {
			// 错误的字符串,无法识别
			throw new IllegalArgumentException("unknow UserAgent string :"
					+ value);
		}
		this.realName = values[0];
		this.province = values[1];
		this.city = values[2];
		long login = Long.parseLong(values[3], 32);
		this.setLoginTime(new Date(login));
		return this;
	}
}
