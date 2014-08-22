package org.tmsframework.mvc.mail.render;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tmsframework.mvc.mail.context.MailContext;
import org.tmsframework.mvc.mail.template.MailTemplateResolver;

/**
 * 邮件渲染抽象类
 */
public abstract class AbstractMailRender implements MailRender {
    
    protected final Log logger = LogFactory.getLog(getClass());
    
    private MailTemplateResolver templateResolver;

    public MailTemplateResolver getTemplateResolver() {
        return templateResolver;
    }

    public void setTemplateResolver(MailTemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }

    public MailContext render(MailContext context) {
        return doRender(context);
    }
    
    protected abstract MailContext doRender(MailContext context);

}
