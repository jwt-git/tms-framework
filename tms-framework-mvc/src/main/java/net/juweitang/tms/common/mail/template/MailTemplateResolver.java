package net.juweitang.tms.common.mail.template;

import java.util.Locale;

/**
 * 邮件模板解析接口，解析模板名为某类具体的邮件模板。
 * 
 * <p>该接口仅解析模板名，不包括邮件模板的渲染。
 * 
 * <p>解析的模板名可能是本地敏感（Locale-Sensitive）的，即指定的某一模板名，在不同的
 * 语言环境下，对应不同语言版本的模板。子类应该尽可能地实现此功能。
 */
public interface MailTemplateResolver {
    
    /**
     * 解析邮件模板名称，根据模板名和本地信息获取某种邮件模板的实现。
     * @param templateName 邮件模板名称
     * @param locale 本地信息
     * @return MailTemplate 邮件模板
     * @throws Exception 解析中产生的异常
     */
    public MailTemplate resolveTemplateName(String templateName, Locale locale) throws Exception;

}
