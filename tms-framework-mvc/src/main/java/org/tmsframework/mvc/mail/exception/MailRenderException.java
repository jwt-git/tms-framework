package org.tmsframework.mvc.mail.exception;

/**
* 邮件渲染异常，继承自 {@link MailException} 。
 * <p>
 * 该类为邮件渲染（包括可配置的邮件解析、模板渲染）中产生的异常提供统一接口。
 * 
 */
public class MailRenderException extends MailException {

    private static final long serialVersionUID = -3393220743503681700L;

    public MailRenderException() {
        super();
    }

    public MailRenderException(String message, Throwable cause) {
        super("render mail error, " + message, cause);
    }

    public MailRenderException(String message) {
        super("render mail error, " + message);
    }

    public MailRenderException(Throwable cause) {
        super(cause);
    }

}
