package com.icanpay.interfaces;

import java.util.Map;

/**
 * 支付订单通过url提交
 * 
 * @author milanyangbo
 *
 */
public interface WapPaymentUrl {

	/**
	 * 创建包含支付订单数据的url地址
	 * @param map
	 * @return
	 */
	String buildWapPaymentUrl(Map<String, String> map);
}
