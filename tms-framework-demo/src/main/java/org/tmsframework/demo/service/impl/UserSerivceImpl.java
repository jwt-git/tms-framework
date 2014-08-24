package org.tmsframework.demo.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.tmsframework.demo.domain.NativePlace;
import org.tmsframework.demo.domain.User;
import org.tmsframework.demo.service.UserService;

@Service("userService")
public class UserSerivceImpl implements UserService {

	public User register(User user) {
		return null;
	}

	public User getUserByNamePasswd(String realName, String password) {
		User u = new User();
		u.setRealName(realName);
		u.setPassword(password);
		u.setBirthday(new Date());
		NativePlace place = new NativePlace();
		place.setProvince("浙江");
		place.setCity("杭州");
		u.setNativePlace(place);
		return u;
	}

}
