package org.tmsframework.mvc.mail.sender;

import org.springframework.mail.MailException;
import org.tmsframework.mvc.mail.context.MailContext;

/**
 * 邮件发送接口，提供从最简单编码方式的邮件发送到可配置的邮件发送统一调用接口。
 * 
 * <p>邮件发送应该包括邮件信息的组装、邮件信息的检查和真正的发送，该接口的子类可能需要
 * 实现根据传入的MailContext组装邮件信息（例如：外部的调用只设置了邮件Id和邮件模板名，
 * 其它信息被配置在某份文件中，这种情况下需要复杂的组装策略）、对组装的邮件信息进行检
 * 查（例如：收件人不能为空）、真正发送邮件。
 * 
 * <p>该接口不依赖任何Java Mail API。其中，原有Java Mail API中的 {@link javax.mail.internet.MimeMessage} 
 * 被 {@link com.hundsun.network.melody.common.mail.context.MailContext} 替代，MailContext
 * 是一个普通的POJO，可以非常方便地被各种框架使用。
 * 
 * <p>发送过程产生的异常将被转换成 {@link com.hundsun.network.melody.common.mail.exception.MailException} ，
 * 这是一个RuntimeException，调用端可以选择合适地处理它。
 * 
 */
public interface MailSender {
    
    /**
     * 发送单个邮件。
     * @param mail 邮件上下文
     * @throws MailException 邮件异常
     * @see com.hundsun.network.melody.common.mail.context.MailContext
     * @see com.hundsun.network.melody.common.mail.exception.MailException
     */
    public void send(MailContext mail) throws MailException;
    
    /**
     * 发送一组邮件。
     * @param mail 邮件上下文
     * @throws MailException 邮件异常
     * @see com.hundsun.network.melody.common.mail.context.MailContext
     * @see com.hundsun.network.melody.common.mail.exception.MailException
     */
    public void send(MailContext[] mails) throws MailException;

}
