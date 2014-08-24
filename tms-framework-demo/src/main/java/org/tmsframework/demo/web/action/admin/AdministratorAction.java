package org.tmsframework.demo.web.action.admin;

import java.io.IOException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tmsframework.demo.access.AdminAccess;
import org.tmsframework.demo.domain.Administer;
import org.tmsframework.demo.domain.AdministratorAgent;
import org.tmsframework.demo.enums.FunctionsEnum;
import org.tmsframework.demo.web.validator.AdministerLoginvalidator;
import org.tmsframework.mvc.common.Crumbs;
import org.tmsframework.mvc.web.cookyjar.Cookyjar;

/**
 * 系统管理员登录退出action
 * 
 * @author fish
 * 
 */
@Controller
@RequestMapping("/admin")
public class AdministratorAction {

	/**
	 * 演示如果直接在action中注入配置文件中设置的值
	 */
	private @Value("${system.devMode}")
	boolean devMode;

	private Validator loginValidator = new AdministerLoginvalidator();

	private Random random = new Random();

	/**
	 * 没有@AdminAccess标签,则表示可任意访问
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void loginPage(@ModelAttribute("admin") Administer admin)
			throws IOException {
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("admin") Administer admin,
			BindingResult result, Cookyjar cookyjar) throws IOException {
		loginValidator.validate(admin, result);
		// 错误回显
		if (result.hasErrors()) {
			return "/admin/login";
		}
		// 校验用户名口令
		AdministratorAgent agent = new AdministratorAgent(admin);
		// 并且获得用户所有的权限
		// List<FunctionsEnum> funs = Arrays.asList(FunctionsEnum.Fun2,
		// FunctionsEnum.Fun6);
		// agent.setFunctions(funs);
		// 随机分配两个权限
		agent.setFunctions(random.nextInt(5));
		agent.setFunctions(random.nextInt(5));
		cookyjar.set(agent);
		cookyjar.set(new Crumbs());
		return "redirect:/admin/main.htm";
	}

	/**
	 * @AdminAccess()　表示登录用户就可访问
	 */
	@AdminAccess()
	@RequestMapping(value = "/main")
	public void main(AdministratorAgent agent, ModelMap model) {
		model.addAttribute("admin", agent);
	}

	/**
	 * @AdminAccess( { FunctionsEnum.Fun1, FunctionsEnum.Fun3
	 *               })　表示有fun1或者fun3的admin才能访问
	 */
	@AdminAccess( { FunctionsEnum.Fun1, FunctionsEnum.Fun3 })
	@RequestMapping(value = "/fun1orfun3")
	public void fun1orfun3() {
	}

	/**
	 * 更新admin属性,先进入编辑页面
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public void load(@RequestParam("id") String id, ModelMap model) {
		// 根据id从数据库中load Administer 对象,此demo就简单点直接new一个吧
		Administer admin = new Administer();
		admin.setId(id);
		admin.setLoginId("我是一个测试");
		admin.setRealName("恒生服装制度很扯淡");
		model.addAttribute("admin", admin);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public void update(@ModelAttribute("admin") Administer admin,
			BindingResult result) {
		// 使用validate校验admin对象
		// 调用service更新user对象
	}
}

