package com.fhtiger.utils.web.MsgAssistant.MsgDefine;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *Msg 短信平台接口定义主接口
 *
 * @author LFH
 * @date 2018年10月29日 16:42
 */
public interface MsgServerDefine<R extends MsgSendResponseDefine,M extends MsgPublishDefine> {
	/**
	 * 返回初始uri;
	 * @return
	 */
	String buildUri();

	/**
	 * 发送消息并返回内容
	 * @param msg
	 * @return
	 */
	R  publish(M msg);


	/**
	 *  checkPhone 验证手机号码是否合法
	 * @param str 需要验证的手机号码
	 *            大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
	 *            此方法中前三位格式有：
	 *            13+任意数
	 *            15+除4的任意数
	 *            18+除1和4的任意数
	 *            17+除9的任意数
	 *            147
	 * @return boolean
	 */
	 static boolean checkPhone(String str) {
		if (str == null) {
			return false;
		}
		String regExp = "^((13[0-9])|(15[^4])|(18[0235-9])|(17[0-8])|(147))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		boolean bool = m.matches();
		// 验证香港手机号码，暂时屏蔽
		/*
		 * if(!bool){
		 * regExp = "^(5|6|8|9)\\d{7}$";
		 * p = Pattern.compile(regExp);
		 * m = p.matcher(str);
		 * bool = m.matches();
		 * }
		 */
		return bool;
	}

	/**
	 *  countMsgNum 计算短信条数
	 * @param content 短信内容
	 * @param length 每条短信长度(中英文计算方式相同,按字算)
	 * @return int
	 */
	 static int countMsgNum(String content, int length) {
		int countChar=content.length();
		return countChar%length>0?countChar/length+1:countChar/length;
	}

	/**
	 * 转换返回值类型为UTF-8格式.
	 * @param is
	 * @return
	 */

	 static String responseToString(InputStream is) {

		StringBuilder sb1 = new StringBuilder();

		byte[] bytes = new byte[4096];

		int size = 0;

		try {

			while ((size = is.read(bytes)) > 0) {

				String str = new String(bytes, 0, size, "UTF-8");

				sb1.append(str);

			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				is.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

		return sb1.toString();

	}


}
