package com.fhtiger.utils.web.MsgAssistant.MsgDefine;

/**
 * 发送的消息实体主接口
 *
 * @author LFH
 * @date 2018年10月29日 16:58
 */
public interface MsgPublishDefine {
	/**
	 * 附加的信息内容组装成uri后续部分
	 * @return
	 */
	String addonUri();

	/**
	 * 返回发送的手机号
	 * @return
	 */
	String getMobile();

	/**
	 * 返回发送内容
	 * @return
	 */
	String getContent();
}
