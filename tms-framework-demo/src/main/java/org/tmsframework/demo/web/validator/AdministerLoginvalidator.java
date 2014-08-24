package org.tmsframework.demo.web.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.tmsframework.demo.domain.Administer;

public class AdministerLoginvalidator implements Validator {


	public boolean supports(Class<?> clazz) {
		return Administer.class.equals(clazz);
	}


	public void validate(Object obj, Errors err) {
		Administer admin = (Administer) obj;
		if (StringUtils.isBlank(admin.getLoginId())) {
			err.rejectValue("loginId", null, null, "请输入登录名");
		}
		if (StringUtils.isBlank(admin.getPassword())) {
			err.rejectValue("password", null, null, "请输入口令");
		}
	}

}
