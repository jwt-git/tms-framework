package org.tmsframework.mvc.mail.sender;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tmsframework.mvc.mail.context.MailContext;
import org.tmsframework.mvc.mail.render.MailRender;
import org.tmsframework.mvc.mail.render.impl.SimpleMailRender;

/**
 * 邮件发送抽象类，定义了邮件信息组装（渲染）、检查和发送的一系列过程。
 * 
 * <p>该类使用 {@link org.tmsframework.mvc.mail.render.impl.SimpleMailRender}
 * 作为默认的邮件信息渲染实现，SimpleMailRender支持代码级别的、非配置形式的邮件渲染。
 * 
 * <p>邮件信息检查仅简单地检查了收件人是否为空。
 * 
 * <p>邮件真正发送过程会进行网络通信和I/O，因此该类提供了可以以异步方式发送邮件，默认
 * 采用JDK的 {@link java.util.concurrent.Executors#newSingleThreadScheduledExecutor()} 
 * 作为Executor，该Executor为无界单线程池，适合并发数少、实时性要求不高的场合。
 * 
 */
public abstract class AbstractMailSender implements MailSender {
    
    private static final Executor DEFAULT_EXECUTOR = Executors.newSingleThreadExecutor();
    
    private static final MailRender DEFAULT_MAIL_RENDER = new SimpleMailRender();
    
    /** 日志 */
    protected final Log logger = LogFactory.getLog(getClass());
    
    /** 是否异步发送邮件 */
    private boolean isAsynchronous = false;
    
    /** 线程Executor */
    private Executor executor = DEFAULT_EXECUTOR;
    
    /** 邮件渲染器 */
    private MailRender mailRender = DEFAULT_MAIL_RENDER;
    

    protected boolean isAsynchronous() {
        return isAsynchronous;
    }

    public void setAsynchronous(boolean isAsynchronous) {
        this.isAsynchronous = isAsynchronous;
    }

    protected Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    protected MailRender getMailRender() {
        return mailRender;
    }

    public void setMailRender(MailRender mailRender) {
        this.mailRender = mailRender;
    }

    public void send(MailContext mail) {
        if (mail == null) {
            throw new IllegalArgumentException("parameter mail can't be null");
        }
        final MailContext renderedMail = doRender(mail);
        checkMail(mail);
        if (isAsynchronous()) {
            executor.execute(new Runnable() {
                public void run() {
                    doSend(renderedMail);
                }
            });
        } else {
            doSend(renderedMail);
        }
    }

    public void send(MailContext[] mails) {
        if (mails == null || mails.length == 0) {
            throw new IllegalArgumentException("parameter mail array can't be null or empty");
        }
        for (MailContext mail : mails) {
            send(mail);
        }
    }
    
    protected MailContext doRender(MailContext mail) {
        if (getMailRender() == null) {
            return mail;
        }
        MailContext renderedMail = mailRender.render(mail);
        return renderedMail;
    }
    
    protected void checkMail(MailContext mail) {
        if (mail != null && (mail.getTo() == null || mail.getTo().length == 0)) {
            throw new IllegalArgumentException("parameter mail to can't be null");
        }
    }

    protected abstract void doSend(MailContext mail);

}
