package com.fhtiger.utils.web.filter;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 清除静态资源中业务模块的缓存,以免开发过程调试麻烦
 * 
 * @author LFH
 * @since 2018年5月7日
 */
public class ClearCacheFilter implements Filter {

	// 匹配忽略列表的正则集合
	private List<Pattern> patterns = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
		String exurl = filterConfig.getInitParameter("exurl");// 忽略的静态资源列表.
		String[] curls = new String[] {};
		if (exurl != null && exurl.trim().length() > 0) {
			curls = exurl.split("\\s*,\\s*");
		}
		for (String ci : curls) {
			if (!ci.startsWith("/")) {
				ci = "/" + ci;
			}
			this.patterns.add(Pattern.compile(ci));
		}
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURI();
		boolean cache = Boolean.parseBoolean(httpRequest.getParameter("cache"));
		boolean ex = this.patterns.stream().anyMatch(i -> i.matcher(url).matches());
		if (cache || ex) {
			filterChain.doFilter(request, response);
			return;
		}
		HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Cache-Control", "no-cache");
		httpResponse.addHeader("Cache-Control", "no-store");
		httpResponse.addHeader("Cache-Control", "must-revalidate");
		httpResponse.addHeader("Pragma", "no-cache");
		httpResponse.setDateHeader("Expires", -1);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    	this.patterns.clear();
    }
}