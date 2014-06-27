package net.juweitang.tms.common.mail.render.impl;

import java.io.StringWriter;

import net.juweitang.tms.common.mail.context.MailContext;
import net.juweitang.tms.common.mail.render.AbstractMailRender;
import net.juweitang.tms.common.mail.template.MailTemplate;

import org.apache.commons.lang.StringUtils;

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
