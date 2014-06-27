package net.juweitang.tms.common.mail.context;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 邮件上下文，包含邮件的元数据信息（如id、发件人、收件人等）和上下文信息（如邮件模板名、数据模型）。
 * <p>该类具备自我复制的功能，复制的内容包括邮件元数据信息，但不包括上下文信息。
 */
public class MailContext implements Serializable {

    private static final long   serialVersionUID     = -5402964428362110461L;
    
    /** 
     * 默认的 {@link #setEncoding(String) 编码} 。
     */
    private static final String DEFAULT_ENCODING     = "UTF-8";
    /** 
     * 默认的{@link #setContentType(String) Content-Type} 。
     */
    private static final String DEFAULT_CONTENT_TYPE = "text/html";
    

    // ~ 邮件元数据 =============================================================
    
    private String              id;
    private String              from;
    private String              replyTo;
    private String[]            to;
    private String[]            cc;
    private String[]            bcc;
    private Date                sentDate;
    private String              subject;
    private String              text;
    private String              encoding             = DEFAULT_ENCODING;
    private String              contentType          = DEFAULT_CONTENT_TYPE;
    
    // ~ 邮件上下文 =============================================================
    
    private String              template;
    private Map<String, Object> model;


    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return this.from;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setTo(String to) {
        this.to = new String[] { to };
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getTo() {
        return this.to;
    }

    public void setCc(String cc) {
        this.cc = new String[] { cc };
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getCc() {
        return cc;
    }

    public void setBcc(String bcc) {
        this.bcc = new String[] { bcc };
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, Object> getModel() {
        if (model == null) {
            model = new HashMap<String, Object>();
        }
        return model;
    }

    public void addAttribute(String attributeName, Object attributeValue) {
        getModel().put(attributeName, attributeValue);
    }

    public void addAllAttributes(Map<String, Object> attributes) {
        getModel().putAll(attributes);
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("SimpleMailMessage: ");
        sb.append("from=").append(this.from).append("; ");
        sb.append("replyTo=").append(this.replyTo).append("; ");
        sb.append("to=").append(this.to).append("; ");
        sb.append("cc=").append(this.cc).append("; ");
        sb.append("bcc=").append(this.bcc).append("; ");
        sb.append("sentDate=").append(this.sentDate).append("; ");
        sb.append("subject=").append(this.subject).append("; ");
        sb.append("text=").append(this.text);
        sb.append("model=").append(this.model);
        return sb.toString();
    }

    public MailContext copy() {
        MailContext mail = new MailContext();
        return mail;
    }

    private static String[] copy(String[] state) {
        String[] copy = new String[state.length];
        System.arraycopy(state, 0, copy, 0, state.length);
        return copy;
    }

}
