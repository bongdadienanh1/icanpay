package com.icanpay.interfaces;

import java.util.Map;

import com.alipay.api.AlipayApiException;

/**
 * 支付订单通过url提交
 * 
 * @author milanyangbo
 *
 */
public interface WapPaymentUrl {

	/**
	 * 创建包含支付订单数据的url地址
	 * 
	 * @param redirect_url
	 *            微信自定义跳转路径
	 * @return
	 * @throws AlipayApiException
	 * @throws Exception
	 */
	String buildWapPaymentUrl(Map<String, String> map);
}
