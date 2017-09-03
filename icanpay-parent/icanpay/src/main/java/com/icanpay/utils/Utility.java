package com.icanpay.utils;

import java.security.MessageDigest;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class Utility {
	/**
	 * 获得字符串的MD5值，MD5值为大写
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String getMD5(String text) throws Exception {
		return getMD5(text, "UTF-8");
	}

	/**
	 * 获得字符串的MD5值，MD5值为大写
	 * 
	 * @param text
	 * @param textEncoding
	 * @return
	 * @throws Exception
	 */
	public static String getMD5(String text, String textEncoding)
			throws Exception {
		java.security.MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(text.getBytes(textEncoding));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100)
					.substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 获取当前时间戳，单位秒
	 * 
	 * @return
	 */
	public static long getCurrentTimestamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 获取当前时间戳，单位毫秒
	 * 
	 * @return
	 */
	public static long getCurrentTimestampMs() {
		return System.currentTimeMillis();
	}

	/**
	 * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
	 * 
	 * @return
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "")
				.substring(0, 32);
	}

	/**
	 * 获取客户端IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
}
