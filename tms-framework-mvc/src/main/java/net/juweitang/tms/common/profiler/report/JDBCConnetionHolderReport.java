package net.juweitang.tms.common.profiler.report;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author sam.zhang
 *
 */
public class JDBCConnetionHolderReport {

	private Map<Thread, JDBCConnetionHolder> holders = new ConcurrentHashMap<Thread, JDBCConnetionHolder>();

	public Collection<JDBCConnetionHolder> getHolders() {
		return this.holders.values();
	}

	public void getConnectionCall(Thread t) {
		this.holders.put(t, new JDBCConnetionHolder(t));
	}

	public void connectionCloseCall(Thread t) {
		this.holders.remove(t);
	}
}
