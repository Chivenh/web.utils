package com.fhtiger.utils.web;

import javax.validation.constraints.NotNull;

/**
 * 返回简单的存在性的值
 *
 * @author LFH
 * @date 2018年09月28日 15:50
 */
public class ExistsResult<T> {
	private boolean exists;
	private T result;

	public ExistsResult(@NotNull T result) {
		this.exists=true;
		this.result = result;
	}

	public ExistsResult(@NotNull boolean exists) {
		this.exists = exists;
	}

	public ExistsResult(boolean exists, T result) {
		this.exists = exists;
		this.result = result;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(@NotNull boolean exists) {
		this.exists = exists;
	}

	public T getResult() {
		return this.exists? result:null;
	}

	public void setResult(@NotNull T result) {
		this.result = result;
	}
}
