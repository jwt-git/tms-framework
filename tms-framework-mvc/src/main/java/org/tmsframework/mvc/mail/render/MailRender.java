package org.tmsframework.mvc.mail.render;

import org.tmsframework.mvc.mail.context.MailContext;

/**
 * 邮件渲染接口，提供根据传入的邮件上下文，渲染发送邮件所需的上下文。
 * <p>
 * 该接口的最简单实现形式为空实现，即传入的邮件上下文已经包含所有发送邮件所需的信息。
 * 最复杂的实现为传入的邮件上下文仅包含一个标识Id，其余信息均为可配置项。
 * <p>
 * 普通邮件客户端可以解析的邮件内容一般为纯文本和HTML。对于HTML内容，一般使用模板存放。
 * 另外，模板中可能包含一些动态显示的内容，需要对应的业务代码预先将动态显示的数据事先
 * 推入邮件上下文中。因此子类可能需要有适合的邮件模板解析器来解析这类模板和数据。
 */
public interface MailRender {
    
    /**
     * 渲染邮件上下文。
     * @param mail 邮件上下文
     * @return MailContext 渲染后的邮件上下文
     */
    public MailContext render(MailContext context);

}
