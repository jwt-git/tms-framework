package org.tmsframework.demo.web.action.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tmsframework.mvc.web.filter.ResponseOutputBufferFilter;

/**
 * 
 * @author dell
 * 
 */
@Controller
public class OutputReportAction {
	@Autowired
	private ResponseOutputBufferFilter responseOutputBufferFilter;

	@RequestMapping("/report/output")
	public void output(ModelMap model) {
		model.put("report", responseOutputBufferFilter.getReport());
	}
}
