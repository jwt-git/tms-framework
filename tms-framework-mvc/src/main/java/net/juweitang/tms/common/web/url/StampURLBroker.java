package net.juweitang.tms.common.web.url;

import java.util.HashMap;
import java.util.Map;

import net.juweitang.tms.common.web.velocity.eventhandler.DirectOutput;

import org.apache.commons.lang.StringUtils;


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
	 * �õ�һ����̬��Դ������,���������stamp��ע�⣺��̬��Դ�������ڵ�һ����ɺ�һֱʹ�ã����ټ���
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
		// ����ʱ�������
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
