package org.tmsframework.mvc.web.velocity.resolver;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

/**
 * @author sam.zhang
 * 
 */
public class FixedVelocityLayoutViewResolver extends VelocityViewResolver {

	private String layoutUrl;

	private String layoutKey;

	private String screenContentKey;

	private String templateEncoding = "UTF-8";

	private VelocityEngine velocityEngine = null;

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public String getTemplateEncoding() {
		return templateEncoding;
	}

	public void setTemplateEncoding(String templateEncoding) {
		this.templateEncoding = templateEncoding;
	}

	public void setLayoutUrl(String layoutUrl) {
		this.layoutUrl = layoutUrl;
	}

	public void setLayoutKey(String layoutKey) {
		this.layoutKey = layoutKey;
	}

	public void setScreenContentKey(String screenContentKey) {
		this.screenContentKey = screenContentKey;
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		FixedVelocityLayoutView view = (FixedVelocityLayoutView) super
				.buildView(viewName);
		view.setContentType(this.getContentType());
		view.setEncoding(this.templateEncoding);
		if (this.velocityEngine != null) {
			view.setVelocityEngine(this.velocityEngine);
		}
		if (this.layoutUrl != null) {
			view.setLayoutUrl(this.layoutUrl);
		}
		if (this.layoutKey != null) {
			view.setLayoutKey(this.layoutKey);
		}
		if (this.screenContentKey != null) {
			view.setScreenContentKey(this.screenContentKey);
		}
		return view;
	}

	@Override
	protected Class requiredViewClass() {
		return FixedVelocityLayoutView.class;
	}

	@Override
	protected Class getViewClass() {
		return FixedVelocityLayoutView.class;
	}
}
