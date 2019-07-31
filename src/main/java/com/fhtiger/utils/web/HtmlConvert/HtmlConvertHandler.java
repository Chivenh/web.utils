package com.fhtiger.utils.web.HtmlConvert;


import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 对静态文件的一些附加处理
 *
 * @author LFH
 * @date 2018年09月27日 12:49
 */
public class HtmlConvertHandler {

	/**
	 * 以流方式输出html页面到浏览器
	 * @param response
	 * @param html
	 */
	public static void print(HttpServletResponse response, File html){
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter writer = response.getWriter();
			String htmlString= FileUtils.readFileToString(html, "UTF-8");
			writer.print(htmlString);
			writer.flush();
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 重定向到指定页面
	 * @param request
	 * @param response
	 * @param html
	 */
	public static void forward(HttpServletRequest request, HttpServletResponse response,File html){
		try {
			String path=html.getAbsolutePath();
			request.getRequestDispatcher(getWebRelativePath(request,path)).forward(request,response);
		} catch (ServletException|IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取生成的静态文件在项目中的相对路径
	 * @param path
	 * @return
	 */
	public static String getWebRelativePath(HttpServletRequest request,String path){
		String base=request.getContextPath();
		base=base.startsWith("/")?base.substring(1):base;
		path=path.replaceAll("\\\\","/").replace(base,"");
		path=path.startsWith("/")?path:"/"+path;
		return path;
	}
}
