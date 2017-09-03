package com.icanpay.interfaces;

import java.util.Map;

/**
 * 创建客户端SDK支付需要信息
 * 
 * @author milanyangbo
 *
 */
public interface AppParams {
	/**
	 * 创建客户端SDK支付需要信息
	 * 
	 * @return
	 * @throws Exception
	 */
	Map<String, String> buildPayParams() throws Exception;
}
