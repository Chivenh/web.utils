package com.fhtiger.utils.web.MsgAssistant;


import com.fhtiger.utils.web.MsgAssistant.MsgDefine.MsgSendResponseDefine;

import java.util.Date;

/**
 * 消息返回响应
 *
 * @author LFH
 * @date 2018年10月29日 16:53
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
	 * @param state
	 * @return
	 */
	public static MsgSendResponse write(int state){
		return new MsgSendResponse(state,null,null);
	}


	/**
	 * @param state
	 * @param mobile
	 * @return
	 */
	public static MsgSendResponse write(int state,String mobile){
		return new MsgSendResponse(state,mobile,null);
	}

	/**
	 * @param state
	 * @param mobile
	 * @param msg
	 * @return
	 */
	public static MsgSendResponse write(int state,String mobile,String msg){
		return new MsgSendResponse(state,mobile,msg);
	}

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
