package com.fhtiger.utils.web.keysfind;
@FunctionalInterface
public interface KeyFormatter {
	/**
	 * 将原始字符串格式化后返回
	 * @param source 原字符串
	 * @return String
	 */
	String format(String source);
}
