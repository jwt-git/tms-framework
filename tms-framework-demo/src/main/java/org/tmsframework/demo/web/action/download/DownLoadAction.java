package org.tmsframework.demo.web.action.download;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author fish
 * 
 */
@Controller
@RequestMapping("/download")
public class DownLoadAction {

	private @Value("${web.encoding}")
	String encoding;

	private String exportContentType = "application/vnd.ms-excel";

	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public void excel(HttpServletResponse response) throws IOException {
		// 作为一个demo,excel使用html内容,而非正式使用中poi等工具生成
		response.setCharacterEncoding(encoding);
		response.setContentType(exportContentType);
		String fileName = "demo_down_load.xls";
		response.setHeader("Content-Disposition", "attachment;Filename="
				+ fileName);
		response
				.getWriter()
				.print(
						"<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset="+encoding+"\"></head><body><table><tr><th>头1</th><th>头2</th></tr><tr><td>行1</td><td>行2</td></tr></table></body></html>");
	}
}