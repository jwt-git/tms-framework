package org.tmsframework.mvc.mail.render.impl;

import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;
import org.tmsframework.mvc.mail.context.MailContext;
import org.tmsframework.mvc.mail.render.AbstractMailRender;
import org.tmsframework.mvc.mail.template.MailTemplate;

public class SimpleMailRender extends AbstractMailRender {

    @Override
    protected MailContext doRender(MailContext mail) {
        if (StringUtils.isNotBlank(mail.getTemplate())) {
            if (getTemplateResolver() == null) {
                throw new IllegalArgumentException("mail template resolver can't be null");
            }
            StringWriter sw = new StringWriter();
            try {
                MailTemplate template = getTemplateResolver().resolveTemplateName(mail.getTemplate(), null);           
                template.render(mail.getModel(), sw);
            } catch (Exception e) {
                logger.error("",e);
            }
            mail.setText(sw.toString());
        }
        return mail;
    }

}
