package com.fhtiger.utils.web;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 客户端甄别
 *
 * @author LFH
 * @since 2019年01月09日 10:41
 */
public final class ClientExamineUtil {

	/**
	 * 获取用户代理对象
	 * @return
	 */
	public static UserAgent getUserAgent(String userAgent){
		return UserAgent.parseUserAgentString(userAgent);
	}

	/**
	 * 获取设备类型
	 * @return
	 */
	public static DeviceType getDeviceType(String userAgent){
		return getUserAgent(userAgent).getOperatingSystem().getDeviceType();
	}

	/**
	 * 是否是PC
	 * @return
	 */
	public static boolean isPc(String userAgent){
		return DeviceType.COMPUTER.equals(getDeviceType(userAgent));
	}

	/**
	 * 是否是手机
	 * @return
	 */
	public static boolean isMobile(String userAgent){
		return DeviceType.MOBILE.equals(getDeviceType(userAgent));
	}

	/**
	 * 是否是平板
	 * @return
	 */
	public static boolean isTablet(String userAgent){
		return DeviceType.TABLET.equals(getDeviceType(userAgent));
	}

	/**
	 * 是否是手机和平板
	 * @return
	 */
	public static boolean isMobileOrTablet(String userAgent){
		DeviceType deviceType = getDeviceType(userAgent);
		return DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType);
	}
}
