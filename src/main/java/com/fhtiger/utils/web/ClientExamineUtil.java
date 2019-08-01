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
	 * @param userAgent 客户端标识
	 * @return  {@link UserAgent}
	 */
	public static UserAgent getUserAgent(String userAgent){
		return UserAgent.parseUserAgentString(userAgent);
	}

	/**
	 * 获取设备类型
	 * @param userAgent 客户端标识
	 * @return {@link DeviceType}
	 */
	public static DeviceType getDeviceType(String userAgent){
		return getUserAgent(userAgent).getOperatingSystem().getDeviceType();
	}

	/**
	 * 是否是PC
	 * @param userAgent 客店端标识
	 * @return boolean
	 */
	public static boolean isPc(String userAgent){
		return DeviceType.COMPUTER.equals(getDeviceType(userAgent));
	}

	/**
	 * 是否是手机
	 * @param userAgent 客户端标识
	 * @return boolean
	 */
	public static boolean isMobile(String userAgent){
		return DeviceType.MOBILE.equals(getDeviceType(userAgent));
	}

	/**
	 * 是否是平板
	 * @param userAgent 客户端标识
	 * @return boolean
	 */
	public static boolean isTablet(String userAgent){
		return DeviceType.TABLET.equals(getDeviceType(userAgent));
	}

	/**
	 * 是否是手机和平板
	 * @param userAgent 客户端标识
	 * @return boolean
	 */
	public static boolean isMobileOrTablet(String userAgent){
		DeviceType deviceType = getDeviceType(userAgent);
		return DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType);
	}
}
