package org.tmsframework.demo.web.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.tmsframework.demo.domain.User;

/**
 * 
 * @author fish
 * 
 */
public class UserLoginValidator implements Validator {

	public boolean supports(Class<?> clz) {
		return User.class.equals(clz);
	}

	public void validate(Object obj, Errors err) {
		User user = (User) obj;
		if (StringUtils.isBlank(user.getRealName())) {
			err.rejectValue("realName", null, null, "请输入姓名");
		}
		if (StringUtils.isBlank(user.getPassword())) {
			err.rejectValue("password", null, null, "请输入口令");
		}
	}

}
