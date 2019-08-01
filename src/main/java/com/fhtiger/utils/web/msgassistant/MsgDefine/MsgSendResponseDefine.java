package com.fhtiger.utils.web.msgassistant.MsgDefine;

/**
 * 消息返回格式预定义
 *
 * @author LFH
 * @since 2018年10月29日 17:39
 */
public abstract class MsgSendResponseDefine {
	protected int state;
	protected String msg;
	protected String mobile;

	protected MsgSendResponseDefine(int state, String mobile,String msg) {
		this.state = state;
		this.msg = msg;
		this.mobile = mobile;
	}


	protected int getState() {
		return state;
	}

	protected String getMsg() {
		return msg;
	}

	protected String getMobile() {
		return mobile;
	}
}
