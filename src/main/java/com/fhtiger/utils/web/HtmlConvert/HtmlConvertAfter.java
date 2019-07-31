package com.fhtiger.utils.web.HtmlConvert;

import java.io.File;

/**
 * ...
 *
 * @author LFH
 * @date 2018年09月27日 11:36
 */
@FunctionalInterface
public interface HtmlConvertAfter {
	void work(File html);
}
