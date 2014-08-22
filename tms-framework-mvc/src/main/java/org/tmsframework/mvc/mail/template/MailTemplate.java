package org.tmsframework.mvc.mail.template;

import java.io.Writer;
import java.util.Map;

/**
 * 邮件模板接口，提供邮件模板的渲染。
 * 
 * <p>邮件模板应该包括模板名、模板内部实现和编码等属性，这些属性在应该子类中包含。
 * 
 */
public interface MailTemplate {
    
    /**
     * 根据传入的数据模型里的数据，渲染邮件模板，并将渲染结果写入字符输出流中。
     * <p>字符输出流在大部分情况下为一个{@link java.io.StringWriter}，这样就可以通过
     * {@link java.io.StringWriter#toString()}方法获得渲染后的字符内容。
     * @param model 数据模型
     * @param writer 字符输出流
     * @throws Exception 渲染时的异常
     */
    public void render(Map<String, ?> model, Writer writer) throws Exception;

}
