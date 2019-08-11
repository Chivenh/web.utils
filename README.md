
## 常用web开发中的各类工具.

```$xml
<dependency>
    <groupId>com.fhtiger.utils</groupId>
    <artifactId>web.utils</artifactId>
    <version>0.0.2-RELEASE</version>
</dependency>
```

# com.fhtiger.utils.web
## emailassistant
```java
    //EmailUtil 是真正用来构建并发送邮件的工具.
    //1.使用 EmailUtil#emailSender 创建发送者对象
    import com.fhtiger.utils.web.emailassistant.EmailUtil;
    class Test{
    	 EmailUtil emailSender = EmailUtil.emailSender("username","password","smtpHost");
    	 //默认smtp端口号为21,如果因部署环境等情况需要更改,使用 EmailUtil#withSmtpPort来设置
    	 emailSender.withSmtpPort(8088);
    	 //EmailBody 可以作为一个中间参数传递对象.比如邮件发送以异步事件或队列方式触发,则可以将EmailBody作为传递者.
        
        //使用sendEmail开头的方法来实际发送邮件.
        emailSender.sendEmail("subject","htmlContent","receivers");
    }
   
```
## filter
    [ClearCacheFilter](./com/fhtiger/utils/web/filter/ClearCacheFilter.md)
## htmlconvert
```java
    import com.fhtiger.utils.web.htmlconvert.*;
    class Test{
    	//HtmlConvertUtil htmlConvertUtil = new HtmlConvertUtil(configuration);
    	HtmlConvertUtil htmlConvertUtil = new HtmlConvertUtil(basePath,configuration);
        
    	//使用HtmlConvertUtil#process方法来生成静态文件.
    	
    }
```
## keysfind
    wait...
## msgassistant
    wait...
## taskmanager
    wait...
