package org.tmsframework.mvc.mail.exception;

/**
  * 邮件异常，继承自 {@link java.lang.RuntimeException} 。
 * <p>
 * 该类为邮件发送、渲染提供了统一的异常接口，所有邮件发送、渲染过程中抛出的底层异常都
 * 应该转换为该类或者其子类，由上层调用接口决定如何处理此类异常。
 */
public class MailException extends RuntimeException {

    private static final long serialVersionUID = -6167680496070147103L;

    public MailException() {
        super();
    }

    public MailException(String message) {
        super(message);
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailException(Throwable cause) {
        super(cause);
    }

}
