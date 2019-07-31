package com.fhtiger.utils.web.KeysFind;

import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 关键字处理工具
 *
 * @author LFH
 * @date 2018年10月15日 11:09
 */
public final class KeyDefineTransfer {
	private Map<Character, Set<KeyDefine>> keys;
	private final @NotNull Set<String> sourceKeys;

	public KeyDefineTransfer(Set<String> sourceKeys) {
		this.sourceKeys = sourceKeys;
		this.init();
	}

	private void init() {
		this.keys = new HashMap<>(this.sourceKeys.size());
		for (String sourceKey : this.sourceKeys) {
			int keyLength = sourceKey.length();
			boolean isEnd = false;
			if (keyLength == 1) {
				isEnd = true;
			}
			char[] sourceKeys = sourceKey.toCharArray();
			Character key = sourceKeys[0];
			if (!this.keys.containsKey(key)) {
				this.keys.put(key, new HashSet<>());
			}
			Set<KeyDefine> keyDefines = this.keys.get(key);
			KeyDefine keyDefine = new KeyDefine(key, isEnd);
			if (isEnd) {
				keyDefine.setSourceKey(sourceKey);
			} else {
				ArrayList<KeyDefine> followKeys = new ArrayList<>();
				for (int i = 1; i < keyLength; i++) {
					KeyDefine keyDefinei = new KeyDefine(sourceKeys[i], false);
					if (i == keyLength - 1) {
						keyDefinei.setEnd(true);
						keyDefinei.setSourceKey(sourceKey);
					}
					followKeys.add(keyDefinei);
				}
				keyDefine.setFollowKeys(followKeys);
			}
			keyDefines.add(keyDefine);
		}
	}

	public Map<Character, Set<KeyDefine>> getKeys() {
		return keys;
	}

	/**
	 *<b>替换文本优先级要高于格式化对象</b>
	 * @param content 原始文本
	 * @param replaceMent 替换字符(即:将关键字以替换字符替换)
	 * @param formatter 格式化对象
	 */
	private KeyFilterResult filterDeal(String content,Character replaceMent,KeyFormatter formatter){
		long start= System.currentTimeMillis();
		Map<Character, Set<KeyDefine>> outKeys = this.getKeys();
		char[] contentArray = content.toCharArray();
		List<ArrayList<KeyDefine>> currentFilters = null;
		boolean isStart = true;
		List<String> errors = new ArrayList<>();
		long length = contentArray.length;
		final boolean hasReplace=replaceMent!=null,hasFormatter=!hasReplace&&formatter!=null;
		char[] copyContent=hasReplace?contentArray.clone():null;
		//每个过滤过程的序号
		int filterIndex=0;
		//过程数据处理
		DataDeal dataDeal=(sourceKey,i)->{
			int sourceKeyLength = sourceKey.length();
			if(hasReplace){
				for(int x=i;x>x-sourceKeyLength;x--){
					copyContent[x]=replaceMent;
				}
			}else if(hasFormatter){
				errors.add((i >= sourceKeyLength ? contentArray[i - sourceKeyLength] : "")+formatter.format(sourceKey)+(i < length - 1 ? contentArray[i + 1] : ""));
			}else{
				errors.add( (i >= sourceKeyLength ? contentArray[i - sourceKeyLength] : "") + "<item>"
						+ sourceKey + "</item>" + (i < length - 1 ? contentArray[i + 1] : ""));
			}
		};
		for (int i = 0; i < length; i++) {
			char c = contentArray[i];
			if (isStart && outKeys.containsKey(c)) {
				currentFilters = new ArrayList<>();
				for (KeyDefine keyDefine : outKeys.get(c)) {
					if (keyDefine.isEnd()) {
						String sourceKey = keyDefine.getSourceKey();
						dataDeal.deal(sourceKey,i);
					} else {
						currentFilters.add(keyDefine.getFollowKeys());
						isStart = false;
					}
				}
			} else if (!isStart && !CollectionUtils.isEmpty(currentFilters)) {
				final int currentFilterIndex=filterIndex;
				currentFilters = currentFilters.stream().filter(fi -> fi.get(currentFilterIndex).getKey().equals(c)).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(currentFilters)) {
					List<ArrayList<KeyDefine>> removes = new ArrayList<>();
					for (ArrayList<KeyDefine> filter : currentFilters) {
						KeyDefine current = filter.get(filterIndex);
						if (current.isEnd()) {
							String currentSourceKey = current.getSourceKey();
							dataDeal.deal(currentSourceKey,i);
							removes.add(filter);
						}
					}
					currentFilters.removeAll(removes);
				}
				if (CollectionUtils.isEmpty(currentFilters)) {
					isStart = true;
					filterIndex=0;
				} else {
					filterIndex++;
				}
			}
		}
		//当有敏感词被过滤到时才为true状态.
		KeyFilterResult keyFilterResult=new KeyFilterResult(!CollectionUtils.isEmpty(errors));
		keyFilterResult.setMatches(errors);
		if(keyFilterResult.isSuccess()&& hasReplace){
			keyFilterResult.setResultContent(new String(copyContent));
		}
		keyFilterResult.setTimeConsuming(System.currentTimeMillis()-start);
		return keyFilterResult;
	}

	/**
	 * 检索文本处理敏感词
	 * @param content 原始文本
	 */
	public KeyFilterResult filterDeal(String content){
		return filterDeal(content,null,null);
	}

	/**
	 * 检索文本处理敏感词
	 * @param content   原始文本
	 * @param replaceMent 替换字符(即:将关键字以替换字符替换)
	 */
	public KeyFilterResult filterDeal(String content,char replaceMent){
		return filterDeal(content,replaceMent,null);
	}

	/**
	 * 检索文本处理敏感词
	 * @param content   原始文本
	 * @param formatter 格式化对象(将检索到的敏感词处理成想要的格式,方便提示)
	 */
	public KeyFilterResult filterDeal(String content,KeyFormatter formatter){
		return filterDeal(content,null,formatter);
	}

	/**
	 * 过程数据处理接口
	 */
	@FunctionalInterface
	private interface  DataDeal{
		void deal(String sourceKey, int i);
	}
}
