package net.juweitang.tms.common.mail.exception;

/**
* 邮件发送异常，继承自 {@link MailException} 。
 * 
 * <p>
 * 该类为邮件发送中产生的异常提供统一的接口。
 */
public class MailSendException extends MailException {

    private static final long serialVersionUID = 4735425732029974285L;

    public MailSendException() {
        super();
    }

    public MailSendException(String message, Throwable cause) {
        super("send mail error, " + message, cause);
    }

    public MailSendException(String message) {
        super("send mail error, " + message);
    }

    public MailSendException(Throwable cause) {
        super(cause);
    }

}
