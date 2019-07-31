package com.fhtiger.utils.web.KeysFind;
@FunctionalInterface
public interface KeyFormatter {
	/**
	 * 将原始字符串格式化后返回
	 * @param source
	 * @return
	 */
	String format(String source);
}
