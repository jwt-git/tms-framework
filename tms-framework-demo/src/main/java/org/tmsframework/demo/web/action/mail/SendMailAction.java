package org.tmsframework.demo.web.action.mail;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tmsframework.mvc.mail.context.MailContext;
import org.tmsframework.mvc.mail.sender.MailSender;

/**
 * 发送邮件Action，该类主要用于演示邮件发送功能。
 * 
 * @author zhengdd
 * @version $Id: LognAction.java,v 0.1 2010-6-23 上午11:30:42 zhengdd Exp $
 */
@Controller
@RequestMapping("/mail")
public class SendMailAction {

    private static final String DEAULT_FROM = "";
    
    // ~ MailSender =============================================================================================
    
    @Autowired
    private MailSender          simpleMailSender;
    @Autowired
    private MailSender          velocityMailSender;
    @Autowired
    private MailSender          velocityLayoutMailSender;
    

    // ~ 简单邮件发送 =============================================================================================

    @RequestMapping(value = "/send-simple-mail", method = GET)
    public void sendSimpleMailInit(@ModelAttribute("mail") MailContext mail) {
        mail.setFrom(DEAULT_FROM);
    }

    /**
     * 最简单形式的邮件发送，使用Spring的Java Mail API，需要向 {@link #simpleMailSender} 
     * 注入 {@link org.springframework.mail.javamail.JavaMailSenderImpl JavaMailSenderImpl}，请参考mail-beans.xml
     * 中的simpleMailSender相关配置。
     * @param mail
     * @return String
     */
    @RequestMapping(value = "/send-simple-mail", method = POST)
    public String sendSimpleMail(@ModelAttribute("mail") MailContext mail) {
        simpleMailSender.send(mail);
        return "mail/success";
    }
    
    
    // ~ 简单的Velocity模板邮件发送 ================================================================================

    @RequestMapping(value = "/send-velocity-mail", method = GET)
    public void sendVelocityMailInit(@ModelAttribute("mail") MailContext mail) {
        mail.setFrom(DEAULT_FROM);
    }

    /**
     * 简单的Velocity模板邮件发送，需要向 {@link #velocityMailSender} 注入
     * {@link org.tmsframework.mvc.mail.render.impl.SimpleMailRender SimpleMailRender} ,
     * SimpleMailRender需要被注入 {@link org.tmsframework.mvc.mail.template.velocity.VelocityMailTemplateResolver VelocityMailTemplateResolver}，
     * 请参考mail-beans.xml中的velocityMailSender相关配置。
     * <p>
     * {@link MailContext#setTemplate(String)} 为设置模板信的名称，可以不写明文件后缀，
     * Velocity模板默认为.vm后缀，模板信应该放置在VelocityEngine中配置的resourceLoaderPath目录
     * 下。
     * <p>
     * {@link MailContext#setModel(java.util.Map)} 为设置数据模型，提供解析模板中变量使用。
     * <p>
     * <b>注意：如果应用中无模板信，可以不用配置MailTemplateResolver。
     * @param mail
     * @param name
     * @return String
     */
    @RequestMapping(value = "/send-velocity-mail", method = POST)
    public String sendVelocityMail(@ModelAttribute("mail") MailContext mail, @RequestParam("name") String name) {
        mail.setTemplate("velocity-mail-test.vm");
        mail.addAttribute("name", name);
        velocityMailSender.send(mail);
        return "mail/success";
    }
    
    
    // ~ Velocity Layout/Screen模板邮件发送 =====================================================================================

    @RequestMapping(value = "/send-velocity-layout-mail", method = GET)
    public void sendVelocityLayoutMailInit(@ModelAttribute("mail") MailContext mail) {
        mail.setFrom(DEAULT_FROM);
    }

    /**
     * Velocity Layout/Screen模板邮件发送。
     * @param mail
     * @param name
     * @return String
     */
    @RequestMapping(value = "/send-velocity-layout-mail", method = POST)
    public String sendVelocityLayoutMail(@ModelAttribute("mail") MailContext mail, @RequestParam("name") String name) {
        mail.setTemplate("velocity-layout-mail-test.vm");
        mail.addAttribute("name", name);
        velocityLayoutMailSender.send(mail);
        return "mail/success";
    }

}