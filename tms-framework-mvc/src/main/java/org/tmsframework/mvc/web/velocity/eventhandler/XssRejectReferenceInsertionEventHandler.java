package org.tmsframework.mvc.web.velocity.eventhandler;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.RuntimeServicesAware;

/**
 * config:
 * eventhandler.referenceinsertion.class=com.nonfamous.framework.web.view
 * .velocity.eventhandler.XssRejectReferenceInsertionEventHandler
 * reference.insertion
 * .event.handler.direct.variable.names=$!direct-output,$body_context,....
 * 
 * @author sam.zhang
 * 
 */
public class XssRejectReferenceInsertionEventHandler implements
		ReferenceInsertionEventHandler, RuntimeServicesAware {

	private static final Log logger = LogFactory
			.getLog(XssRejectReferenceInsertionEventHandler.class);
	private static final String DirectOutputVariableConfiguration = "reference.insertion.event.handler.direct.variable.names";

	private Set<String> directOutputVariables = new HashSet<String>();

	public Object referenceInsert(String reference, Object value) {
		if (value == null) {
			return value;
		}
		if (directOutputVariables.contains(reference)) {
			return value;
		}
		if (value instanceof DirectOutput) {
			return value;
		}
		if (!(value instanceof String)) {
			return value;
		}
		return StringEscapeUtils.escapeHtml((String) value);
	}

	public void setRuntimeServices(RuntimeServices rs) {
		String[] temp = rs.getConfiguration().getStringArray(
				DirectOutputVariableConfiguration);
		if (temp != null && temp.length > 0) {
			for (String s : temp) {
				if (StringUtils.isNotBlank(s)) {
					directOutputVariables.add(s.trim());
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("init DirectOutputVariable with:"
					+ directOutputVariables);
		}
	}
}
