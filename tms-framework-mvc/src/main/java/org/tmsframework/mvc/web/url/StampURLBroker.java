package org.tmsframework.mvc.web.url;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.tmsframework.mvc.web.velocity.eventhandler.DirectOutput;


/**
 * 
 * @author sam.zhang
 * 
 */
public class StampURLBroker extends URLBroker {

	private String stamp;

	private String stampName = "t";

	private Map<String, StaticQueryData> urlCache = new HashMap<String, StaticQueryData>();

	/**
	 * 得到一个静态资源的链接,如果有配置stamp。注意：静态资源的链接在第一次生成后将一直使用，不再计算
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public QueryData setTarget(String resource) {
		if (resource == null) {
			throw new NullPointerException("resource can't be null.");
		}
		if (urlCache.containsKey(resource)) {
			return urlCache.get(resource);
		}
		QueryData qd = super.setTarget(resource);
		// 附加时间戳参数
		String appendStamp = null;
		if (StringUtils.isNotBlank(stamp)) {
			int i = resource.lastIndexOf('.');
			if (i != -1) {
				appendStamp = stamp + resource.substring(i);
			}
		}

		if (appendStamp != null) {
			qd.addQueryData(stampName, appendStamp);
		}
		StaticQueryData back = new StaticQueryData(qd.toString());
		urlCache.put(resource, back);
		return back;
	}

	public class StaticQueryData extends QueryData implements DirectOutput {
		private String resouceUrl;

		public StaticQueryData(String resouceUrl) {
			this.resouceUrl = resouceUrl;
		}

		@Override
		public String toString() {
			return resouceUrl;
		}

	}

	public String getStamp() {
		return stamp;
	}

	public void setStamp(String stamp) {
		this.stamp = stamp;
	}

	public String getStampName() {
		return stampName;
	}

	public void setStampName(String stampName) {
		this.stampName = stampName;
	}

}
