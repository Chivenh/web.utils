package com.fhtiger.utils.web.htmlconvert;

import java.io.File;

/**
 * 静态文件生成后续操作
 *
 * @author LFH
 * @since 2018年09月27日 11:36
 */
@FunctionalInterface
public interface HtmlConvertAfter {
	void work(File html);
}
