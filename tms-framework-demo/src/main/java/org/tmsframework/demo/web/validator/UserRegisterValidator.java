package org.tmsframework.demo.web.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.tmsframework.demo.domain.User;

/**
 * 登陆校验，实现Spring的Validator接口，采用编码的形式验证。
 * 
 * @author zhengdd
 * @version $Id: UserValidator.java,v 0.1 2010-6-23 上午11:34:29 zhengdd Exp $
 */
public class UserRegisterValidator implements Validator {

    public boolean supports(Class<?> clz) {
        return User.class.equals(clz);
    }

    /**
     * 验证处理
     * @param obj
     * @param err
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object obj, Errors err) {
        Assert.notNull(obj);
        Assert.isInstanceOf(User.class, obj);
        User user = (User) obj;
        String realName = user.getRealName();
        if (StringUtils.isBlank(realName)) {
            // 4个参数分别为：验证对象的属性名、错误信息代码、错误信息参数、默认错误信息。
            // 错误信息可以通过配置MessageResource，指定错误信息代码，而无需在代码里写死错误信息。
            err.rejectValue("realName", null, null, "请填写姓名");
        } else {
            if (realName.length() > 16) {
                err.rejectValue("realName", null, new Integer[]{16}, "姓名不能超过{0}个字符");
            }
        }
        if(StringUtils.isBlank(user.getPassword())){
        	 err.rejectValue("password", null, null, "请输入口令");
        }
    }

}