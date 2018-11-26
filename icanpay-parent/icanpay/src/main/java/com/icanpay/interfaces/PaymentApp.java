package com.icanpay.interfaces;

import java.util.Map;

/**
 * 手机端SDK支付
 *
 * @author milanyangbo
 */
public interface PaymentApp {

	/**
	 * 创建手机端SDK支付需要信息
	 *
	 * @return
	 */
	Map<String, String> buildPayParams();
}
