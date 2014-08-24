package org.tmsframework.demo.web.action;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tmsframework.demo.domain.User;
import org.tmsframework.demo.domain.UserAgent;
import org.tmsframework.demo.service.UserService;
import org.tmsframework.demo.web.validator.UserLoginValidator;
import org.tmsframework.mvc.common.Crumbs;
import org.tmsframework.mvc.web.cookyjar.Cookyjar;

/**
 * 
 * @author fish
 * 
 */

@Controller
public class UserLoginoutAction {
	@Autowired
	private UserService userService;

	private Validator loginValidator = new UserLoginValidator();

	@RequestMapping(value = "/login", method = GET)
	public void loginPage(@ModelAttribute("user") User user) {
	}

	@RequestMapping(value = "/login", method = POST)
	public String register(@ModelAttribute("user") User user,
			BindingResult result, Cookyjar cookyjar) {
		loginValidator.validate(user, result);
		// 错误回显
		if (result.hasErrors()) {
			return "login";
		}
		// 逻辑检查
		User u = userService.getUserByNamePasswd(user.getRealName(), user
				.getPassword());
		UserAgent userAgent = new UserAgent(u);
		userAgent.setLoginTime(new Date());
		cookyjar.set(userAgent);
		cookyjar.set(new Crumbs());
		return "redirect:/user_main.htm";
	}

	/**
	 * 偷懒一下,user main就写在loginoutAction
	 */
	@RequestMapping("/user_main.htm")
	public void main(UserAgent agent, ModelMap model) {
		model.addAttribute("agent", agent);
		// 显示首页需要的逻辑...
	}

}
