package org.tmsframework.mvc.mail.template.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.tmsframework.mvc.mail.template.AbstractCachedMailTemplateResolver;
import org.tmsframework.mvc.mail.template.MailTemplate;

/**
 * 基于Velocity的邮件模板解析类，包含一个Velocity的邮件模板实现类。
 * 
 * <p>
 * 该类实现了Velocity的邮件模板解析，需要设置Velocity Engine用于解析。
 */
public class VelocityMailTemplateResolver extends AbstractCachedMailTemplateResolver {
    
    private static final String DEFAULT_VELOCITY_PROPERTIES_PATH = "velocity.properties";
    
    /**
     * 默认的 {@link #setVelocityEngine(VelocityEngine) Velocity Engine}。
     */
    private static final VelocityEngine DEFAULT_VELOCITY_ENGINE;

    static {
        Properties pros = new Properties();
        try {
            InputStream is = VelocityMailTemplateResolver.class.getResourceAsStream(DEFAULT_VELOCITY_PROPERTIES_PATH);
            pros.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load 'velocity.properties'", e);
        }
        if (pros.isEmpty()) {
            throw new IllegalStateException("velocity.properties can't be empty");
        }
        VelocityEngine engine;
        try {
            engine = new VelocityEngine(pros);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
        DEFAULT_VELOCITY_ENGINE = engine;
    }

    /** Velocity Engine */
    private VelocityEngine velocityEngine = DEFAULT_VELOCITY_ENGINE;

    /**
     * 设置Velocity Engine。
     */
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    /**
     * 获取Velocity Engine。
     */
    protected VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    @Override
    protected MailTemplate loadTemplate(String templateName, Locale locale) throws Exception {
        VelocityMailTemplate mailTemplate = new VelocityMailTemplate(templateName);
        // TODO; 加入其它初始化代码
        return mailTemplate;
    }

    /**
     * 基于Velocity的邮件模板实现类。
     */
    protected class VelocityMailTemplate extends AbstractMailTemplate {

        private Template template;

        public VelocityMailTemplate(String templateName) {
            this.name = templateName;
        }

        protected Template getTemplate() {
            Template template = null;
            try {
                template = getVelocityEngine().getTemplate(name);
            } catch (ResourceNotFoundException e) {
                logger.error("", e);
            } catch (ParseErrorException e) {
                logger.error("", e);
            } catch (Exception e) {
                logger.error("", e);
            }
            return template;
        }

        protected Template getTemplate(String name) {
            Template template = null;
            try {
                template = getVelocityEngine().getTemplate(name);
            } catch (ResourceNotFoundException e) {
                logger.error("", e);
            } catch (ParseErrorException e) {
                logger.error("", e);
            } catch (Exception e) {
                logger.error("", e);
            }
            return template;
        }

        protected Context createVelocityContext(Map<String, Object> model) throws Exception {
            return new VelocityContext(model);
        }

        protected void mergeTemplate(Context context, Writer writer) throws Exception {
            if (template == null) {
                template = getTemplate();
            }
            try {
                template.merge(context, writer);
            } catch (Exception ex) {
                // TODO;
                throw new Exception();
            }
        }

        @Override
        protected void mergeTemplateModel(Map<String, Object> model, Writer writer) throws Exception {
            Context velocityContext = createVelocityContext(model);
            mergeTemplate(velocityContext, writer);
        }

    }

}
