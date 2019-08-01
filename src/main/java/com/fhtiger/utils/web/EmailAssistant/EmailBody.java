package com.fhtiger.utils.web.EmailAssistant;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 邮件实体
 *
 * @author LFH
 * @since 2018年10月31日 11:53
 */
public final class EmailBody {
	/*邮件标题*/
	private @NotNull String subject;
	/*邮件正文*/
	private @NotNull String htmlContent;
	/*邮件接收人集合*/
	private @NotNull List<String> receivers;
	/*抄送人集合*/
	private List<String> ccEmails;
	/*附件路径集合*/
	private List<String> paths;

	private EmailBody(String subject, String htmlContent, List<String> receivers, List<String> ccEmails,
			List<String> paths) {
		this.subject = subject;
		this.htmlContent = htmlContent;
		this.receivers = receivers;
		this.ccEmails = ccEmails;
		this.paths = paths;
	}

	public static EmailBody build(String subject, String htmlContent, List<String> recivers){
		return new EmailBody(subject,htmlContent,recivers,null,null);
	}


	public static EmailBody buildWithCopy(String subject, String htmlContent, List<String> recivers, List<String> ccEmails){
		return new EmailBody(subject,htmlContent,recivers,ccEmails,null);
	}


	public static EmailBody buildWithAttachment(String subject, String htmlContent, List<String> recivers, List<String> paths){
		return new EmailBody(subject,htmlContent,recivers,null,paths);
	}

	public String getSubject() {
		return subject;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public List<String> getReceivers() {
		return receivers;
	}

	public List<String> getCcEmails() {
		return ccEmails;
	}

	public List<String> getPaths() {
		return paths;
	}
}
