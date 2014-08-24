package org.tmsframework.demo.web.action.query;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tmsframework.common.query.QueryBase;
import org.tmsframework.demo.query.UserQuery;
import org.tmsframework.demo.web.action.PageSlider;

@Controller
@RequestMapping("/query")
public class Page {
	@RequestMapping("/form_page")
	public void formPage(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "apage", required = false) Integer pageAgain,
			ModelMap model) throws Exception {
		// 模拟分页的情况
		UserQuery query = new UserQuery();
		query.setTotalItem(9999);
		if (pageAgain != null) {
			query.setCurrentPage(pageAgain);
		} else {
			query.setCurrentPage(page);
		}
		model.put("query", query);
	}
	

	@RequestMapping("/seo_page")
	public void seoPage(ModelMap model) throws Exception {
		this.page("abc", "eeee", 1, model);
	}

	@RequestMapping("/{paramOne}/{paramTwo}/{page}")
	public String page(@PathVariable("paramOne") String paramOne,
			@PathVariable("paramTwo") String paramTwo,
			@PathVariable(value = "page") int page, ModelMap model)
			throws Exception {
		// ģ���ҳ�����
		UserQuery query = new UserQuery();
		query.setTotalItem(9999);
		query.setCurrentPage(page);
		model.put("query", query);
		model.put("builder", new PageSlider.PagingURLBuilder() {
			public String build(QueryBase query, int page) {
				StringBuilder sb = new StringBuilder("/query/");
				sb.append("abc").append('/').append("eeee");
				sb.append('/').append(page).append(".htm");
				return sb.toString();
			}
		});
		return "/query/seo_page";
	}
}
