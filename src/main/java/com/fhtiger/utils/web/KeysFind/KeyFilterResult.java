package com.fhtiger.utils.web.KeysFind;

import java.io.Serializable;
import java.util.List;

/**
 *	过滤结果返回
 *
 * @author LFH
 * @date 2018年10月16日 09:43
 */
public final class KeyFilterResult implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 成功与否
	 */
	private boolean success=false;

	/**
	 * 匹配结果集
	 */
	private List<String> matches;

	/**
	 * 如果有替换操作,返回结果文本
	 */
	private String resultContent;

	/**
	 * 执行耗时(ms)
	 */
	private long timeConsuming;

	public KeyFilterResult(boolean success, List<String> matches, String resultContent) {
		this.success = success;
		this.matches = matches;
		this.resultContent = resultContent;
	}

	public KeyFilterResult(boolean success, List<String> matches) {
		this.success = success;
		this.matches = matches;
	}

	public KeyFilterResult(boolean success) {
		this.success = success;
	}

	public KeyFilterResult() {
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<String> getMatches() {
		return matches;
	}

	public void setMatches(List<String> matches) {
		this.matches = matches;
	}

	public String getResultContent() {
		return resultContent;
	}

	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}

	public long getTimeConsuming() {
		return timeConsuming;
	}

	public void setTimeConsuming(long timeConsuming) {
		this.timeConsuming = timeConsuming;
	}
}
