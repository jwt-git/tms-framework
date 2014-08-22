package org.tmsframework.mvc.web.url;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tmsframework.mvc.web.velocity.eventhandler.DirectOutput;


/**
 * 
 * @author sam.zhang
 * 
 */
public class URLBroker {
	private final Log logger = LogFactory.getLog(this.getClass());

	protected String encoding = "UTF-8";

	protected URLConfig config;

	// ����"/" ��β
	protected String server = null;

	public void init(String contextPath) {
		if (this.server != null) {// �Ѿ�init����
			return;
		}
		StringBuilder server = new StringBuilder();
		server.append(config.getURL());
		if (config.isFollowContextPath()) {
			server.append(contextPath);
		}
		if (server.charAt(server.length() - 1) == '/') {
			server.deleteCharAt(server.length() - 1);
		}

		if (StringUtils.isNotBlank(config.getPath())) {
			if (config.getPath().startsWith("/")) {
				server.append(config.getPath());
			} else {
				server.append('/').append(config.getPath());
			}
		}

		if (server.charAt(server.length() - 1) == '/') {
			server.deleteCharAt(server.length() - 1);
		}
		this.server = server.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("init end,server:" + this.server);
		}
	}

	public QueryData setTarget(String target) {
		return new QueryData(target);
	}

	public QueryData get(String target) {
		return setTarget(target);
	}

	@Override
	public String toString() {
		return this.server;
	}

	public class QueryData implements DirectOutput {
		protected StringBuilder query;

		protected QueryData() {
		}

		protected QueryData(String target) {
			if (target == null) {
				return;
			}
			query = new StringBuilder();
			query.append(URLBroker.this.server);
			if (target.startsWith("/")) {
				query.append(target);
			} else {
				query.append('/').append(target);
			}
			query.append('?');
		}

		public QueryData addQueryData(String id, String value) {
			query.append(id).append('=');
			try {
				query.append(URLEncoder.encode(value, URLBroker.this.encoding));
			} catch (UnsupportedEncodingException e) {
				if (logger.isErrorEnabled()) {
					logger.error("UnsupportedEncoding:"
							+ URLBroker.this.encoding, e);
				}
			}
			query.append('&');
			return this;
		}

		public QueryData addQueryData(String id, long value) {
			return addQueryData(id, String.valueOf(value));
		}

		public QueryData addQueryData(String id, Object value) {
			return addQueryData(id, String.valueOf(value));
		}

		public QueryData addQueryData(String id, double value) {
			return addQueryData(id, String.valueOf(value));
		}

		public QueryData addQueryData(String id, int value) {
			return addQueryData(id, String.valueOf(value));
		}

		public QueryData addQueryDatas(Map<String, Object> parameters) {
			if (parameters == null || parameters.isEmpty()) {
				return this;
			}
			for (Entry<String, Object> entry : parameters.entrySet()) {
				this.addQueryData(entry.getKey(), entry.getValue());
			}
			return this;
		}

		@Override
		public String toString() {
			if (this.query == null) {
				return URLBroker.this.toString();
			}
			char last = this.query.charAt(this.query.length() - 1);
			if (last == '?' || last == '&') {
				return this.query.deleteCharAt(this.query.length() - 1)
						.toString();
			}
			return this.query.toString();
		}

	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public URLConfig getConfig() {
		return config;
	}

	public void setConfig(URLConfig config) {
		this.config = config;
	}

}
