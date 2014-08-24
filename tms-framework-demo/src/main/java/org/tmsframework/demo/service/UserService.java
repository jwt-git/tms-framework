package org.tmsframework.demo.service;

import org.tmsframework.demo.domain.User;

/**
 * 用户Service接口
 * 
 * @author zhengdd
 * @version $Id: UserService.java,v 0.1 2010-6-24 上午11:08:53 zhengdd Exp $
 */
public interface UserService {

	/**
	 * 注册用户并获取注册后的信息
	 * 
	 * @param user
	 * @return User
	 */
	public User register(User user);

	/**
	 * 根据用户名称和明文口令获得用户
	 * 
	 * @param realName
	 * @param password
	 * @return
	 */
	public User getUserByNamePasswd(String realName, String password);

}
