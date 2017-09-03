package com.icanpay.interfaces;

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
	 * @throws Exception
	 */
	String buildWapPaymentUrl(String redirect_url) throws Exception;
}
