package net.juweitang.tms.common.mail.template;

import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 带缓存的邮件模板解析抽象类，包含了一个邮件模板的抽象实现。
 * 
 * <p>
 * 解析邮件模板是将邮件模板名转化为某种模板技术实现的一个过程，而邮件模板一般为磁盘
 * 上的某一文件，需要进行I/O操作。该类提供了缓存邮件模板的功能，将首次解析成功的邮件模 板缓存，减少之后再次调用解析的I/O操作。
 * 
 * <p>
 * 该类还提供了设置全局属性的接口，子类可以通过接口设置全局属性，这些属性默认会被推 入渲染邮件模板所需的数据模型中。
 * 
 * <p>
 * 该类包含了一个默认的邮件模板抽象实现。
 * 
 */
public abstract class AbstractCachedMailTemplateResolver implements MailTemplateResolver {

    /** 日志 */
    protected final Log                     logger           = LogFactory.getLog(getClass());

    /** 邮件模板缓存映射 */
    private final Map<Object, MailTemplate> templateCache    = new HashMap<Object, MailTemplate>();

    /** 是否缓存邮件模板 */
    private boolean                         cache            = true;

    /** 全局属性 */
    private Map<String, Object>             globalAttributes = new HashMap<String, Object>();

    /**
     * 设置全局属性，这些属性会在邮件模板渲染之前，被注入到渲染所需的数据模型中。
     */
    public void setGlobalAttributes(Map<String, Object> globalAttributes) {
        addGlobalAttributes(globalAttributes);
    }

    public void addGlobalAttribute(String key, Object value) {
        if (this.globalAttributes.containsKey(key)) {
            logger.debug("a attribute will be overrided, the key: " + key);
        }
        this.globalAttributes.put(key, value);
    }

    public void addGlobalAttributes(Map<String, Object> globalAttributes) {
        for (Map.Entry<String, Object> attribute : globalAttributes.entrySet()) {
            if (this.globalAttributes.containsKey(attribute.getKey())) {
                logger.debug("a attribute will be overrided, the key: " + attribute.getKey());
            }
            this.globalAttributes.put(attribute.getKey(), attribute.getValue());
        }
    }

    /**
     * 开启/关闭邮件模板缓存。
     * <p>
     * 默认为 "true"：开启缓存。
     * <p>
     * <b>注意：关闭邮件模板缓存会极大地影响性能，建议仅在开发或者调试模式下关闭。</b>
     */
    public void setCache(boolean cache) {
        this.cache = cache;
    }

    /**
     * 返回是否开启邮件模板缓存。
     */
    public boolean isCache() {
        return this.cache;
    }

    /**
     * 根据给出的模板名和区域返回邮件模板缓存key。
     * <p>
     * 默认返回为包含模板名和区域后缀的的字符串，子类可以覆盖该方法。
     */
    protected Object getCacheKey(String templateName, Locale locale) {
        if (locale == null) {
            return templateName;
        } else {
            return templateName + "_" + locale;
        }
    }

    /**
     * 提供清除特定邮件模板缓存的功能。
     */
    public void removeFromCache(String templateName, Locale locale) {
        if (!this.cache) {
            logger.warn("View caching is SWITCHED OFF -- removal not necessary");
        } else {
            Object cacheKey = getCacheKey(templateName, locale);
            Object cachedView;
            synchronized (this.templateCache) {
                cachedView = this.templateCache.remove(cacheKey);
            }
            if (cachedView == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("No cached instance for view '" + cacheKey + "' was found");
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Cache for view " + cacheKey + " has been cleared");
                }
            }
        }
    }

    /**
     * 清空邮件模板缓存。
     */
    public void clearCache() {
        logger.debug("Clearing entire view cache");
        synchronized (this.templateCache) {
            this.templateCache.clear();
        }
    }

    public MailTemplate resolveTemplateName(String templateName, Locale locale) throws Exception {
        if (!isCache()) {
            return loadTemplate(templateName, locale);
        } else {
            Object cacheKey = getCacheKey(templateName, locale);
            synchronized (this.templateCache) {
                MailTemplate template = this.templateCache.get(cacheKey);
                if (template == null) {
                    template = loadTemplate(templateName, locale);
                    this.templateCache.put(cacheKey, template);
                }
                return template;
            }
        }
    }

    /**
     * 子类必须实现该方法，构建特定的模板，返回的模板将会被缓存。
     * <p>
     * 子类可以忽略传入的本地信息，即不要求强制实现解析过程中的本地敏感特性，但是 建议尽量实现。
     * 
     * @param templateName
     *            邮件模板名称
     * @param locale
     *            本地信息
     * @return the 特定的模板实现
     * @throws Exception
     *             解析过程中产生的异常
     */
    protected abstract MailTemplate loadTemplate(String templateName, Locale locale) throws Exception;

    /**
     * 邮件模板抽象类，提供子类实现特定的模板技术。
     * <p>
     * {@link AbstractCachedMailTemplateResolver#globalAttributes}将被推入
     * {@link #render(Map, Writer)} 的数据模型中。
     */
    protected abstract class AbstractMailTemplate implements MailTemplate {

        protected String name;

        public void render(Map<String, ?> model, Writer writer) throws Exception {
            Map<String, Object> mergedModel = new HashMap<String, Object>(globalAttributes.size()
                                                                          + (model != null ? model.size() : 0));
            if (model != null) {
                mergedModel.putAll(model);
            }
            for (Entry<String, Object> attribute : globalAttributes.entrySet()) {
                if (mergedModel.containsKey(attribute.getKey())) {
                    logger.debug("a attribute will be overrided, key: " + attribute.getKey());
                }
                mergedModel.put(attribute.getKey(), attribute.getValue());
            }
            mergeTemplateModel(mergedModel, writer);
        }

        /**
         * 合并邮件模板和数据对象，将渲染结果写入字符输出流中，子类必须实现该方法。
         * 
         * @param model
         *            数据对象
         * @param writer
         *            字符输出流
         * @throws Exception
         *             合并或者写入流时产生的异常
         */
        protected abstract void mergeTemplateModel(Map<String, Object> model, Writer writer) throws Exception;

    }

}
