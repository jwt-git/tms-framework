package org.tmsframework.mvc.mail.render.impl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.tmsframework.mvc.mail.context.MailContext;
import org.tmsframework.mvc.mail.exception.MailRenderException;
import org.tmsframework.mvc.mail.render.AbstractMailRender;
import org.tmsframework.mvc.mail.template.MailTemplate;

/**
 * Spring Bean邮件渲染类，
 */
public class SpringBeanMailRender extends AbstractMailRender implements ApplicationContextAware {

    /** 邮件上下文持有者 */
    private final Map<String, MailContext> mailContextHolder = new HashMap<String, MailContext>();

    /** Spring的应用上下文 */
    private ApplicationContext             applicationContext;

    /** 是否严格要求邮件上下文必须为Spring Bean配置 */
    private boolean                        strict            = false;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        initMailContexts();
    }

    /**
     * 初始化所有配置为Spring Bean的邮件上下文。
     */
    protected void initMailContexts() {
        Map<String, MailContext> mailContextBeans = applicationContext.getBeansOfType(MailContext.class);
        for (Entry<String, MailContext> mail : mailContextBeans.entrySet()) {
            mailContextHolder.put(mail.getKey(), mail.getValue());
        }
    }

    /**
     * 设置是否严格要求邮件上下文必须为Spring Bean配置，默认“false”，为不严格要求。
     * <p>
     * 如果设置为“true”，会检查每个邮件上下文是否被配置为Spring Bean。
     * <p>
     * 在一些场合中，需要对应用内所有的邮件进行统一配置管理，可以设置为“true”。
     */
    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    /**
     * 获取是否严格要求邮件上下文必须为Spring Bean配置
     */
    protected boolean isStrict() {
        return strict;
    }

    @Override
    protected MailContext doRender(MailContext mail) {
        if (isStrict()) {
            if (mail.getId() == null) {
                throw new MailRenderException("mail context id can't be null");
            }
            if (!mailContextHolder.containsKey(mail.getId())) {
                throw new MailRenderException("can't find the mail context bean, bean id: " + mail.getId());
            }
            mail = mailContextHolder.get(mail.getId());
        }

        if (StringUtils.isNotBlank(mail.getTemplate())) {
            if (getTemplateResolver() == null) {
                throw new MailRenderException("mail template resolver can't be null");
            }
            StringWriter sw = new StringWriter();
            try {
                MailTemplate template = getTemplateResolver().resolveTemplateName(mail.getTemplate(), null);
                template.render(mail.getModel(), sw);
            } catch (Exception e) {
                logger.error("", e);
            }
            mail.setText(sw.toString());
        }
        
        return mail;
    }

}
