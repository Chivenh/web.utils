package com.fhtiger.utils.web.msgassistant.MsgDefine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *Msg 短信平台接口定义主接口
 *
 * @author LFH
 * @since 2018年10月29日 16:42
 * @param <R> {@link MsgSendResponseDefine}
 * @param <M> {@link MsgPublishDefine}
 */
public interface MsgServerDefine<R extends MsgSendResponseDefine,M extends MsgPublishDefine> {

	 static Logger logger= LogManager.getLogger(MsgServerDefine.class);

	/**
	 * 返回初始uri;
	 * @return String
	 */
	String buildUri();

	/**
	 * 发送消息并返回内容
	 * @param msg {@link MsgPublishDefine}
	 * @return {@link MsgSendResponseDefine}
	 */
	R  publish(M msg);

	/**
	 * 不校验香港手机号码
	 * @param str 手机号码
	 * @return boolean
	 */
	 static boolean checkPhone(String str) {
		return checkPhone(str,false);
	}

	/**
	 *  <p>验证手机号码是否合法</p>
	 *  <pre>
	 * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
	 * 	  此方法中前三位格式有：
	 * 	 13+任意数
	 * 	 15+除4的任意数
	 * 	 18+除1和4的任意数
	 * 	 17+除9的任意数
	 * 	 147
	 *  </pre>
	 * @param str 需要验证的手机号码
	 * @param hkCheck 是否校验香港手机号码
	 * @return boolean
	 */
	static boolean checkPhone(String str,boolean hkCheck){
		if (str == null) {
			return false;
		}
		String regExp = "^((13[0-9])|(15[^4])|(18[0235-9])|(17[0-8])|(147))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		boolean bool = m.matches();
		// 验证香港手机号码
		 if(!bool&&hkCheck){
		 regExp = "^[5689]\\d{7}$";
		 p = Pattern.compile(regExp);
		 m = p.matcher(str);
		 bool = m.matches();
		 }
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
	 * @param is 返回流
	 * @return String
	 */

	 static String responseToString(InputStream is) {

		StringBuilder sb1 = new StringBuilder();

		byte[] bytes = new byte[4096];

		int size = 0;

		try {

			while ((size = is.read(bytes)) > 0) {

				String str = new String(bytes, 0, size, StandardCharsets.UTF_8);

				sb1.append(str);

			}

		} catch (IOException e) {

			logger.error("error:{0}",e);

		} finally {

			try {

				is.close();

			} catch (IOException e) {

				logger.error("error:{0}",e);

			}

		}

		return sb1.toString();

	}


}
