package com.icanpay.interfaces;

import java.util.Map;

import com.alipay.api.AlipayApiException;

/**
 * 手机端SDK支付
 * 
 * @author milanyangbo
 *
 */
public interface AppParams {
	/**
	 * 创建手机端SDK支付需要信息
	 * 
	 * @return
	 * @throws AlipayApiException
	 * @throws Exception
	 */
	Map<String, String> buildPayParams();
}
