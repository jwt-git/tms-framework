package org.tmsframework.util.html.format;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.cyberneko.html.HTMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author sam.zhang
 * 
 */
public class HTMLFormaterImpl implements HTMLFormater {

	private Set<String> acceptElements;

	private Set<String> removeElements;

	private Set<String> attributeMatchers;

	private String encoding = "UTF-8";

	private ThreadLocal<NekoFormater> local = new ThreadLocal<NekoFormater>();

	public String formatHTML(String origHTML) {
		if (StringUtils.isBlank(origHTML)) {
			return origHTML;
		}
		NekoFormater formater = local.get();
		if (formater == null) {
			formater = new NekoFormater(acceptElements, removeElements,
					attributeMatchers, encoding);
			local.set(formater);
		}
		return formater.filter(origHTML);
	}

	public Set<String> getAcceptElements() {
		return acceptElements;
	}

	public void setAcceptElements(Set<String> acceptElements) {
		this.acceptElements = acceptElements;
	}

	public Set<String> getRemoveElements() {
		return removeElements;
	}

	public void setRemoveElements(Set<String> removeElements) {
		this.removeElements = removeElements;
	}

	public Set<String> getAttributeMatchers() {
		return attributeMatchers;
	}

	public void setAttributeMatchers(Set<String> attributeMatchers) {
		this.attributeMatchers = attributeMatchers;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	private static class NekoFormater {
		//
		private static Logger log = LoggerFactory.getLogger(NekoFormater.class);

		private String encoding;

		// 处理缓冲区的最大长度16k
		private static int MAX_BUFFER_LENGTH = 1024 * 16;

		// 过滤器
		private XMLParserConfiguration parser;

		// 数据输出缓冲区
		private ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream(
				MAX_BUFFER_LENGTH);

		/**
		 * 构造函数
		 * 
		 * @param acceptElements
		 * @param removeElements
		 * @param attributeMatchers
		 */
		private NekoFormater(Set<String> acceptElements,
				Set<String> removeElements, Set<String> attributeMatchers,
				String encoding) {
			this.encoding = encoding;
			try {
				// 初始化过滤器
				ElementRemover remover = new ElementRemover();

				// 初始化需要接受的标签
				for (String accept : acceptElements) {
					//
					String[] ret = accept.split(",");
					String[] attrs = null;
					if (ret.length > 1) {
						attrs = (String[]) ArrayUtils.subarray(ret, 1,
								ret.length);
					}
					remover.acceptElement(ret[0], attrs);
				}

				// 初始化需要过滤的标签
				for (String remove : removeElements) {
					remover.removeElement(remove);
				}

				// 初始化用正则表达式的属性检查器
				for (String item : attributeMatchers) {
					String[] ret = item.split(",", 3);
					remover.acceptElementAttribute(ret[0], ret[1], ret[2]);
				}

				// 初始化html配置对象
				parser = new HTMLConfiguration();

				// create writer filter
				org.cyberneko.html.filters.Writer writer = new org.cyberneko.html.filters.Writer(
						outputBuffer, "utf-8");

				// setup filter chain
				XMLDocumentFilter[] filters = { remover, writer };

				//
				parser.setProperty(
						"http://cyberneko.org/html/properties/names/elems",
						"lower");
				parser
						.setProperty(
								"http://cyberneko.org/html/properties/filters",
								filters);

				//
				parser
						.setFeature(
								"http://apache.org/xml/features/scanner/notify-builtin-refs",
								true);
				parser
						.setFeature(
								"http://cyberneko.org/html/features/scanner/notify-builtin-refs",
								true);
				parser
						.setFeature(
								"http://cyberneko.org/html/features/scanner/ignore-specified-charset",
								true);

				//
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 过滤一段原始的HTML
		 * 
		 * @param origHtml
		 * @return
		 */
		private String filter(String origHtml) {
			try {
				// 清空输出缓冲区
				outputBuffer.reset();
				// 非常奇怪，字符串以 &* 结尾的时候，解析会出错
				origHtml = origHtml + ' ';
				XMLInputSource is = new XMLInputSource(null, null, null,
						new StringReader(origHtml), encoding);
				parser.parse(is);
				String back = new String(outputBuffer.toByteArray(), encoding);
				back = back.trim();
				return back;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}

