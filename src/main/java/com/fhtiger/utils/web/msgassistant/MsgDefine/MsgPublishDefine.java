package com.fhtiger.utils.web.msgassistant.MsgDefine;

/**
 * 发送的消息实体主接口
 *
 * @author LFH
 * @since 2018年10月29日 16:58
 */
public interface MsgPublishDefine {
	/**
	 * 附加的信息内容组装成uri后续部分
	 * @return String
	 */
	String addonUri();

	/**
	 * 返回发送的手机号
	 * @return String
	 */
	String getMobile();

	/**
	 * 返回发送内容
	 * @return Strings
	 */
	String getContent();
}
