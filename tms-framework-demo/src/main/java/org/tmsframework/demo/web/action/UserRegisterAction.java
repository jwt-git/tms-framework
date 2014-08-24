package org.tmsframework.demo.web.action;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.tmsframework.demo.enums.ResourceType.CITY;
import static org.tmsframework.demo.enums.ResourceType.PROVINCE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tmsframework.demo.domain.Resource;
import org.tmsframework.demo.domain.User;
import org.tmsframework.demo.service.ResourceService;
import org.tmsframework.demo.service.UserService;
import org.tmsframework.demo.web.validator.UserRegisterValidator;

/**
 * 注册Action，该类主要用于演示一个完整的表单初始化、校验和处理过程。
 * 
 * @author zhengdd
 * @version $Id: LognAction.java,v 0.1 2010-6-23 上午11:30:42 zhengdd Exp $
 */
@Controller
public class UserRegisterAction {

	// ~ Validator
	// ======================================================================

	/** 编码实现的注册Validator */
	private Validator registerValidator = new UserRegisterValidator();
	/**
	 * Spring
	 * Valang实现的注册Validator，验证规则请参考/WEB-INF/conf/spring/web/web-validator.xml
	 * 中id为registerValidator的bean。
	 */
	/*
	 * @Autowired private Validator registerValidator;
	 */

	// ~ Service
	// ========================================================================

	/** Service的注入声明 */
	@Autowired
	private UserService userService;
	@Autowired
	private ResourceService resourceService;

	// ~ 表单初始化
	// =======================================================================

	/**
	 * 初始化绑定。
	 * <p>
	 * 
	 * @InitBinder这个注解用于绑定各种PropertyEditor，即将表单提交过来的字符串， 
	 *                                                   根据注册到WebDataBinder中的PropertyEditor转换为具体的某一类对象
	 *                                                   。
	 *                                                   <p>
	 *                                                   例如：CustomDateEditor 将符合
	 *                                                   "yyyy-MM-dd"
	 *                                                   格式的字符串转换为Data类型。
	 *                                                   <p>
	 *                                                   <b>注意：<br>
	 *                                                   
	 *                                                   1、该方法仅在Controller初始化的时候执行一次
	 *                                                   ；<br>
	 *                                                   
	 *                                                   2、标记有该注解的方法入参必须要有WebDataBinder
	 *                                                   。</b>
	 * 
	 * @param binder
	 */
	@SuppressWarnings("unused")
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		// 注册日期格式化类型: yyyy-MM-dd
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	/**
	 * 初始化省份数据。
	 * <p>
	 * 
	 * @ModelAttribute这个注解用在方法级别上，将会被该方法所属的Controller中所有 
	 *                                                   标注有@RequestMapping的方法自动调用，
	 *                                                   并且将返回值按照@ModelAttribute中指定的
	 *                                                   value添加到Model中。
	 * 
	 * @return List<Resource>
	 */
	@SuppressWarnings("unused")
	@ModelAttribute("provinces")
	private List<Resource> buildProvinces() {
		return resourceService.getResourcesByType(PROVINCE);
	}

	/**
	 * 初始化城市数据。
	 * 
	 * @return List<Resource>
	 */
	@SuppressWarnings("unused")
	@ModelAttribute("cities")
	private List<Resource> buildCities() {
		return resourceService.getResourcesByType(CITY);
	}

	/**
	 * 注册用户信息初始化。
	 * 
	 * @param user
	 */
	@RequestMapping(value = "/register", method = GET)
	public void registerInit(@ModelAttribute("user") User user) {

		// TODO; 加入其它自定义的初始化代码
	}

	// ~ 表单验证、处理
	// ==================================================================

	/**
	 * 注册用户信息。
	 * 
	 * @param user
	 * @param result
	 * @return String
	 */

	@RequestMapping(value = "/register", method = POST)
	public String register(@ModelAttribute("user") User user,
			BindingResult result) {

		// 校验注册用户信息
		registerValidator.validate(user, result);
		// 错误回显
		if (result.hasErrors()) {
			return "register";
		}

		// 注册用户
		userService.register(user);
		// TODO; 加入其它的业务逻辑

		// 成功跳转
		return "success";
	}

}
