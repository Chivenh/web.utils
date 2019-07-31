package com.fhtiger.utils.web.KeysFind;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * ...
 *
 * @author LFH
 * @date 2018年10月15日 10:45
 */
public final class KeyDefine {
	private @NotNull Character key;//关键字
	private @NotNull boolean end=false;//是否最后一个关键字
	private String sourceKey;//所属源关键词[当没有后续关键字时才可以获取]
	private ArrayList<KeyDefine> followKeys;//后续关键字
	public Character getKey() {
		return key;
	}

	public ArrayList<KeyDefine> getFollowKeys() {
		return end?null: followKeys;
	}

	public void setFollowKeys(ArrayList<KeyDefine> followKeys) {
		this.followKeys = followKeys;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public KeyDefine(Character key, ArrayList<KeyDefine> followKeys, boolean end) {
		this.key = key;
		this.followKeys = followKeys;
		this.end = end;
	}

	public KeyDefine(Character key, boolean end) {
		this.key = key;
		this.end = end;
	}

	public KeyDefine(Character key) {
		this.key = key;
	}

	public String getSourceKey() {
		return end?sourceKey: null;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

	@Override public String toString() {
		return "KeyDefine{" + "key=" + key + ", end=" + end + ", sourceKey='" + sourceKey + '\'' + ", followKeys="
				+ followKeys + '}';
	}
}
