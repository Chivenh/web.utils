package com.fhtiger.utils.web.htmlconvert;

import com.fhtiger.utils.web.ExistsResult;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;

/**
 * 使用模板生成html文件的工具
 *
 * @author LFH
 * @since 2018年09月26日 22:37
 */
public class HtmlConvertUtil {

	private Logger logger= LogManager.getLogger(this.getClass());

	private String basePath="";
	private String suffix=".html";
	private String templateSuffix=".ftl";
	private Configuration configuration;

	public HtmlConvertUtil(String basePath, Configuration configuration) {
		this.basePath = basePath;
		this.configuration = configuration;
		prepare();
	}

	public HtmlConvertUtil(Configuration configuration) {
		this.configuration = configuration;
		prepare();
	}

	private void prepare(){
		if(this.basePath!=null&&this.basePath.length()>0){
			if(!this.basePath.endsWith("/")){
				this.basePath+="/";
			}
			File path=new File(this.basePath);
			if(!path.exists()){
				path.mkdirs();
			}
		}
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setTemplateSuffix(String templateSuffix) {
		this.templateSuffix = templateSuffix;
	}

	public String getBasePath() {
		return basePath;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getTemplateSuffix() {
		return templateSuffix;
	}

	/**
	 * 按模板生成文件
	 * @param name 模板名称(不带后缀)
	 * @param fileName 文件名称(不带后缀)
	 * @param data 附加数据
	 * @param after  添加后续操作
	 * @return boolean
	 */
	public boolean process(String name,String fileName, Map<String,Object> data, HtmlConvertAfter after){
		boolean b=false;
		try {
			Template template=this.configuration.getTemplate(name+this.templateSuffix);
			File html=new File(this.basePath+fileName+this.suffix);
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(html),"UTF-8"));
//			Writer out=new FileWriter(html);
			template.process(data,out);
			out.flush();
			out.close();
			if(after!=null){
				after.work(html);
			}
			b=true;
		} catch (MalformedTemplateNameException|ParseException|TemplateException|TemplateNotFoundException e) {
			logger.error("error:{0}",e);
		}catch (IOException e){
			logger.error("io.error:{0}",e);
		}
		return b;
	}

	/**
	 * @param name 模板名称(不带后缀),同时文件名默认使用模板名称
	 * @param data 附加数据
	 * @return boolean
	 */
	public boolean process(String name, Map<String,Object> data){
		return process(name,name,data,null);
	}

	/**
	 * @param name 模板名称(不带后缀),同时文件名默认使用模板名称
	 * @param data 附加数据
	 * @param after {@link HtmlConvertAfter} 文件生成后操作
	 * @return boolean
	 */
	public boolean process(String name, Map<String,Object> data, HtmlConvertAfter after){
		return process(name,name,data,after);
	}

	/**
	 * @param name 模板名称(不带后缀)
	 * @param fileName 生成文件名
	 * @param data 附加数据
	 * @return boolean
	 */
	public boolean process(String name,String fileName, Map<String,Object> data){
		return process(name,fileName,data,null);
	}

	/**
	 * 判断静态文件是否已存在
	 * @param name 文件名
	 * @return {@link ExistsResult}
	 */
	public ExistsResult<File> isExists(String name){
		File file=new File(this.basePath+name+this.suffix);
		return new ExistsResult<File>( file.exists(),file);
	}

	/**
	 * 获取存在的静态文件
	 * @param name 文件名
	 * @return {@link File}
	 */
	public File getExistsFile(String name){
		File file=new File(this.basePath+name+this.suffix);
		return file.exists()?file:null;
	}
}
