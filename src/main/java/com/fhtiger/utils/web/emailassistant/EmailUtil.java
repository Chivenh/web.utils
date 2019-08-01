package com.fhtiger.utils.web.emailassistant;

import com.fhtiger.utils.helperutils.util.Tutil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.internet.MimeUtility;
import java.util.List;

/**
 * 邮件发送工具类
 * @version 0.0.1
 * @author LFH
 */
public final class EmailUtil {
	// 邮箱服务器的登录用户名
	private String username;
	// 邮箱服务器的密码
	private String password;
	// 邮箱服务器smtp host
	private String smtpHost;

	private int smtpPort=25;

	// 发送方的邮箱（必须为邮箱服务器的登录用户名）
	private String fromEmail;
	// 发送方姓名
	private String fromUsername;
	// 邮件内容编码，防止乱码
	private static final String CHARSET = "UTF-8";
	// 默认邮箱服务器
	private static final String SMTPHOST_DEFAULT ="smtp.163.com";// "mail.csc-cq.com.cn";

	private Logger logger= LogManager.getLogger(this.getClass());

	/**
	 * 邮件发送前校验!
	 * @param receiverEmails 接收人邮箱地址
	 * @param ccEmails 抄送人邮箱地址
	 * @param bccEmails 密送人邮箱地址
	 * @return {@link EmailCheck}
	 */
	private EmailCheck prepare(List<String> receiverEmails,List<String> ccEmails, List<String> bccEmails){
		// 简单校验
		if (null == username || "".equals(username.trim())) {
			return new EmailCheck( "用户名为空");
		}
		if (null == password || "".equals(password.trim())) {
			return new EmailCheck("密码为空");
		}
		String[] sm = username.split("@");
		if (sm.length < 2) {
			return new EmailCheck("发件箱格式不对");
		}

		if (CollectionUtils.isEmpty(receiverEmails) && CollectionUtils.isEmpty(ccEmails)
				&& CollectionUtils.isEmpty(bccEmails)) {
			return new EmailCheck("没有任何收件人、密送人、抄送人");
		}

		return EmailCheck.PassCheck;
	}

	/**
	 * 发送邮件(简单邮件,无附件,无抄送)
	 * 
	 * @param subject 邮件主题或标题
	 * @param htmlContent 邮件内容html格式
	 * @param reciveEmils 收件人
	 * @return {@link EmailResponse}
	 */
	public EmailResponse sendEmail(String subject, String htmlContent, List<String> reciveEmils){
		// 简单校验
		EmailCheck check=this.prepare(reciveEmils,  null,  null);
		if(!check.isPass()){
			return new EmailResponse( check.getMsg(),false);
		}
		return this.sendEmail(subject, htmlContent, reciveEmils, null, null, null);
	}

	/**
	 * 发送邮件(简单邮件,无附件)
	 *
	 * @param subject 邮件主题或标题
	 * @param htmlContent 邮件内容html格式
	 * @param reciveEmils 收件人
	 * @param ccEmails    抄送方
	 * @return {@link EmailResponse}
	 */
	public EmailResponse sendEmailWithCopy(String subject, String htmlContent, List<String> reciveEmils, List<String> ccEmails){
		// 简单校验
		EmailCheck check=this.prepare(reciveEmils,  ccEmails,  null);
		if(!check.isPass()){
			return new EmailResponse( check.getMsg(),false);
		}
		return this.sendEmail(subject, htmlContent, reciveEmils, ccEmails, null, null);
	}

	/**
	 * 发送邮件(简单邮件)
	 *
	 * @param subject 邮件主题或标题
	 * @param htmlContent 邮件内容html格式
	 * @param reciveEmils 收件人
	 * @param paths     附件路径
	 * @return {@link EmailResponse}
	 */
	public EmailResponse sendEmailWithAttachment(String subject, String htmlContent, List<String> reciveEmils, List<String> paths){
		// 简单校验
		EmailCheck check=this.prepare(reciveEmils,  null,  null);
		if(!check.isPass()){
			return new EmailResponse( check.getMsg(),false);
		}
		return this.sendEmail(subject, htmlContent, reciveEmils, null, null, paths);
	}

	/**
	 * 发送邮件
	 * <p>Examples</p>
	 * <p><code>SimpleEmail email = new SimpleEmail();</code>//创建简单邮件,不可附件、HTML文本等</p>
	 * <p><code>MultiPartEmail email = new MultiPartEmail();</code>//创建能加附件的邮件,可多个、网络附件亦可</p>
	 * <p>ImageHtmlEmail HTML文本的邮件、通过2代码转转内联图片,网上都是官网上例子而官网上例子比较模糊</p>
	 * <p><code>ImageHtmlEmail email=new ImageHtmlEmail();</code></p>
	 * <p>特殊设置:</p>
	 *  <code>
	 *      email.setDebug(true);//是否开启调试默认不开启
	 * 		email.setSSLOnConnect(true);//开启SSL加密
	 * 		email.setStartTLSEnabled(true);//开启TLS加密
	 * 	</code>
	 * <pre>
	 *email.setAuthentication("aaa","111111");//如果smtp服务器需要认证的话，在这里设置帐号、密码
	 *
	 *	  HtmlEmail、ImageHtmlEmail有setHtmlMsg()方法，且可以直接内联图片,可网上都搞那么复杂说不行如
	 *	  //<img src='http://www.apache.org/images/asf_logo_wide.gif' />本人测试新浪、搜狐、QQ邮箱等都能显示
	 *
	 *	  //如果使用setMsg()传邮件内容，则HtmlEmail内嵌图片的方法
	 *	  URL url = new URL("http://www.jianlimuban.com/图片");
	 *	  String cid = email.embed(url, "名字");
	 *	  email.setHtmlMsg("<img src='cid:"+cid+"' />");
	 *
	 * //这是ImageHtmlEmail的内嵌图片方法，我多次测试都不行，官网提供比较模糊，而大家都是用官网举的例子
	 * // 内嵌图片,此处会抛出MessagingException, MalformedURLException异常
	 *  URL url=new URL("http://www.apache.org");//定义你基本URL来解决相对资源的位置
	 * email.setDataSourceResolver(new DataSourceUrlResolver(url));//这样HTML内容里如果有此路径下的图片会直接内联
	 *
	 * email.buildMimeMessage();//构建内容类型 ，
	 *   //设置内容的字符集为UTF-8,先buildMimeMessage才能设置内容文本 ,但不能发送HTML格式的文本
	 * email.getMimeMessage().setText("<font color='red'>测试简单邮件发送功能！</font>","UTF-8");
	 * </pre>
	 * @param subject 邮件主题或标题
	 * @param htmlContent 邮件内容html格式
	 * @param reciveEmils 收件人列表
	 * @param ccEmails 抄送人列表
	 * @param bccEmails 秘密抄送人列表
	 * @param paths 文件路径
	 * @return {@link EmailResponse}
	 */
	private EmailResponse sendEmail( String subject, String htmlContent,
			List<String> reciveEmils, List<String> ccEmails, List<String> bccEmails, List<String> paths) {
		String flag = "发送失败!";
		EmailResponse response=new EmailResponse(flag,false);
		try {
			// 设置smtp host服务器
			this.smtpHost = Tutil.getStr(this.smtpHost, SMTPHOST_DEFAULT) ;

			// 创建能加附件内容为HTML文本的邮件、HTML直接内联图片但必须用setHtmlMsg()传邮件内容
			HtmlEmail email = new HtmlEmail();

			email.setSmtpPort(this.smtpPort);

			email.setHostName(this.smtpHost);
			// 登录邮件服务器的用户名和密码（保证邮件服务器POP3/SMTP服务开启）
			email.setAuthentication(this.username, this.password);

			email.setFrom(this.username, Tutil.getStr(this.fromUsername,this.username));// 设置发信的邮件帐号和发信人
			// 添加收件人
			if (null != reciveEmils) {
				// 收件人也可以为空
				for (String reciver : reciveEmils) {
					email.addTo(reciver, reciver);// 接收方邮箱和用户名
				}
			}
			// 抄送方
			if (null != ccEmails) {
				for (String CcEmail : ccEmails) {
					email.addCc(CcEmail, CcEmail);// 抄送方
				}
			}
			// 秘密抄送
			if (null != bccEmails) {
				for (String BCcEmail : bccEmails) {
					email.addBcc(BCcEmail, BCcEmail);// 秘密抄送方
				}
			}
			email.setCharset(CHARSET);// 设置邮件的字符集为UTF-8防止乱码
			email.setSubject(subject);// 主题
			email.setHtmlMsg(htmlContent);// 邮件内容

			// 创建邮件附件可多个
			if (null != paths && paths.size() > 0) {
				for (String path : paths) {
					EmailAttachment attachment = new EmailAttachment();
					attachment.setPath(path);
					path = path.substring(path.replaceAll("\\\\", "/").lastIndexOf("/") + 1, path.length());
					attachment.setName(MimeUtility.encodeText(path));// 防止中文乱码
					attachment.setDisposition(EmailAttachment.ATTACHMENT);
					email.attach(attachment);// 添加附件到邮件,可添加多个
				}
			}

			email.send();// 发送邮件
			flag="发送成功";
			response.setMsg(flag);
			response.setSuccess(true);
		} catch (Exception e) {
			logger.error("邮件发送错误!",e);
			response.setMsg("邮件发送错误!");
		}
		return response;
	}

	/**
	 * 邮件发送的构造器
	 *
	 * @param username 发送人的用户名
	 * @param fromUsername 发送方的(名称)签名
	 * @param password 发送人邮箱密码
	 * @param smtpHost SMTP服务器
	 */
	private EmailUtil(String username,String fromUsername, String password, String smtpHost) {
		this.username = username;
		this.fromUsername = fromUsername;
		this.password = password;
		this.smtpHost = smtpHost;
	}


	private EmailUtil(String username, String password, String smtpHost) {
		this(username,username,password,smtpHost);
	}
	private EmailUtil(String username, String password) {
		this(username,username,password,SMTPHOST_DEFAULT);
	}

	/**
	 * 初始化一个邮件发送者
	 * @param username 用户名
	 * @param password 密码
	 * @return {@link EmailUtil}
	 */
	public static EmailUtil emailSender(String username, String password){
		return new EmailUtil(username,password);
	}

	/**
	 * 初始化一个邮件发送者
	 * @param username 用户名
	 * @param password 密码
	 * @param smtpHost smtpHost
	 * @return {@link EmailUtil}
	 */
	public static EmailUtil emailSender(String username, String password,String smtpHost){
		return new EmailUtil(username,password,smtpHost);
	}

	/**
	 * 初始化一个邮件发送者
	 * @param username 用户名
	 * @param sign 签名
	 * @param password 密码
	 * @param smtpHost smtpHost
	 * @return {@link EmailUtil}
	 */
	public static EmailUtil emailSender(String username,String sign, String password,String smtpHost){
		return new EmailUtil(username,sign,password,smtpHost);
	}

	/**
	 * 向邮件发送者附加设置请求端口号
	 * @param smtpPort smtp请求端口号
	 * @return {@link EmailUtil}
	 */
	public EmailUtil withSmtpPort(int smtpPort){
		this.smtpPort=smtpPort;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public String getFromUsername() {
		return fromUsername;
	}

	/**
	 * 邮件校验返回信息
	 */
	private static class EmailCheck{
		private String msg;
		private boolean pass;

		private static EmailCheck PassCheck=new EmailCheck("通过初始校验!",true);

		private EmailCheck(String msg, boolean pass) {
			this.msg = msg;
			this.pass = pass;
		}

		private EmailCheck(String msg) {
			this.msg = msg;
		}

		private String getMsg() {
			return msg;
		}

		private boolean isPass() {
			return pass;
		}
	}

	/**
	 * 邮件发送返回数据
	 */
	public static class EmailResponse{
		private String msg;
		private boolean success;

		private EmailResponse(String msg, boolean success) {
			this.msg = msg;
			this.success = success;
		}

		private EmailResponse(boolean success) {
			this.success = success;
		}

		public String getMsg() {
			return msg;
		}

		private void setMsg(String msg) {
			this.msg = msg;
		}

		public boolean isSuccess() {
			return success;
		}

		private void setSuccess(boolean success) {
			this.success = success;
		}
	}
}
