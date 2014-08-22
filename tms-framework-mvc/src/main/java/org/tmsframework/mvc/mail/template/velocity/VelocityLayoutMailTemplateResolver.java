package org.tmsframework.mvc.mail.template.velocity;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.tmsframework.mvc.mail.template.MailTemplate;

/**
 * 基于Velocity Layout/Screen结构的邮件模板解析类，包含一个Velocity Layout/Screen
 * 结构的邮件模板实现类。
 */
public class VelocityLayoutMailTemplateResolver extends VelocityMailTemplateResolver {

    /** 
     * 默认的 {@link #setLayoutUrl(String) layout url}。 
     */
    private static final String DEFAULT_LAYOUT_URL         = "layout.vm";

    /** 
     * 默认的 {@link #setLayoutKey(String) layout key}。 
     */
    private static final String DEFAULT_LAYOUT_KEY         = "layout";

    /** 
     * 默认的 {@link #setScreenContentKey(String) screen content key}。 
     */
    private static final String DEFAULT_SCREEN_CONTENT_KEY = "screen_content";

    private static final String DEFAULT_LAYOUT_PREFIX      = "layout/";

    private static final String DEFAULT_SCREEN_PREFIX      = "screen/";

    /** layout url */
    private String              layoutUrl                  = DEFAULT_LAYOUT_URL;

    private String              layoutKey                  = DEFAULT_LAYOUT_KEY;

    private String              screenContentKey           = DEFAULT_SCREEN_CONTENT_KEY;

    private String              layoutPrefix               = DEFAULT_LAYOUT_PREFIX;

    private String              screeanPrefix              = DEFAULT_SCREEN_PREFIX;

    /**
     * 设置layout模板. 默认为 {@link #DEFAULT_LAYOUT_URL "layout.vm"}.
     * @param layoutUrl 模板的路径（从模板的根路径起）
     */
    public void setLayoutUrl(String layoutUrl) {
        this.layoutUrl = layoutUrl;
    }

    protected String getLayoutUrl() {
        return layoutUrl;
    }

    /**
     * Set the context key used to specify an alternate layout to be used instead
     * of the default layout. Screen content templates can override the layout
     * template that they wish to be wrapped with by setting this value in the
     * template, for example:<br>
     * <code>#set( $layout = "MyLayout.vm" )</code>
     * <p>Default key is {@link #DEFAULT_LAYOUT_KEY "layout"}, as illustrated above.
     * @param layoutKey the name of the key you wish to use in your
     * screen content templates to override the layout template
     */
    public void setLayoutKey(String layoutKey) {
        this.layoutKey = layoutKey;
    }

    protected String getLayoutKey() {
        return layoutKey;
    }

    /**
     * Set the name of the context key that will hold the content of
     * the screen within the layout template. This key must be present
     * in the layout template for the current screen to be rendered.
     * <p>Default is {@link #DEFAULT_SCREEN_CONTENT_KEY "screen_content"}:
     * accessed in VTL as <code>$screen_content</code>.
     * @param screenContentKey the name of the screen content key to use
     */
    public void setScreenContentKey(String screenContentKey) {
        this.screenContentKey = screenContentKey;
    }

    protected String getScreenContentKey() {
        return screenContentKey;
    }

    protected String getLayoutPrefix() {
        return layoutPrefix;
    }

    public void setLayoutPrefix(String layoutPrefix) {
        this.layoutPrefix = layoutPrefix;
    }

    protected String getScreeanPrefix() {
        return screeanPrefix;
    }

    public void setScreeanPrefix(String screeanPrefix) {
        this.screeanPrefix = screeanPrefix;
    }
    
    @Override
    protected MailTemplate loadTemplate(String templateName, Locale locale) throws Exception {
        VelocityLayoutMailTemplate mailTemplate = new VelocityLayoutMailTemplate(templateName);
        return mailTemplate;
    }

    /**
     * 基于layout/screen方式的Velocity邮件模板实现类。
     */
    protected class VelocityLayoutMailTemplate extends VelocityMailTemplate {

        public VelocityLayoutMailTemplate(String templateName) {
            super(getScreeanPrefix() + templateName);
        }
        
        @Override
        protected void mergeTemplate(Context context, Writer writer) throws Exception {
            renderScreenContent(context);

            // Velocity context now includes any mappings that were defined
            // (via #set) in screen content template.
            // The screen template can overrule the layout by doing
            // #set( $layout = "MyLayout.vm" )
            String layoutUrlToUse = (String) context.get(getLayoutKey());
            if (layoutUrlToUse != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Screen content template has requested layout [" + layoutUrlToUse + "]");
                }
            } else {
                // 如果不指定layout，则使用预定义的layout
                layoutUrlToUse = getLayoutUrl();
            }

            try {
                getTemplate(getLayoutPrefix() + layoutUrlToUse).merge(context, writer);
            } catch (Exception ex) {
                // TODO;
                throw new Exception();
            }
        }

        private void renderScreenContent(Context velocityContext) throws Exception {
            if (logger.isDebugEnabled()) {
                logger.debug("Rendering screen content template [" + name + "]");
            }

            StringWriter sw = new StringWriter();
            Template screenContentTemplate = getTemplate();
            screenContentTemplate.merge(velocityContext, sw);

            velocityContext.put(getScreenContentKey(), sw.toString());
        }
    }

}
