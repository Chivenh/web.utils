package com.fhtiger.utils.web.msgassistant;


import com.fhtiger.utils.web.msgassistant.MsgDefine.MsgSendResponseDefine;

import java.util.Date;

/**
 * 消息返回响应
 *
 * @author LFH
 * @since 2018年10月29日 16:53
 */
public final class MsgSendResponse extends MsgSendResponseDefine {
	private String content;
	private int count;
	private Date sendDate;

	private MsgSendResponse(int state, String mobile, String msg) {
		super(state, mobile, msg);
	}

	private MsgSendResponse(int state, String mobile, String msg, String content, int count, Date sendDate) {
		super(state, mobile, msg);
		this.content = content;
		this.count = count;
		this.sendDate = sendDate;
	}

	/**
	 * @param state 状态值
	 * @return {@link MsgSendResponse}
	 */
	public static MsgSendResponse write(int state){
		return new MsgSendResponse(state,null,null);
	}


	/**
	 * @param state  状态值
	 * @param mobile 手机号
	 * @return {@link MsgSendResponse}
	 */
	public static MsgSendResponse write(int state,String mobile){
		return new MsgSendResponse(state,mobile,null);
	}

	/**
	 * @param state  状态值
	 * @param mobile   手机号
	 * @param msg 状态消息
	 * @return {@link MsgSendResponse}
	 */
	public static MsgSendResponse write(int state,String mobile,String msg){
		return new MsgSendResponse(state,mobile,msg);
	}

	/**
	 * @param content 消息内容
	 * @param count 消息计数
	 * @param sendDate 发送日期
	 * @return {@link MsgSendResponse}
	 */
	public MsgSendResponse withSendInfo(String content, int count, Date sendDate){
		this.content=content;
		this.count=count;
		this.sendDate=sendDate;
		return this;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	@Override public String toString() {
		return "MsgSendResponse{" + "content='" + content + '\'' + ", count=" + count + ", sendDate=" + sendDate
				+ ", state=" + state + ", msg='" + msg + '\'' + ", mobile='" + mobile + '\'' + '}';
	}
}
