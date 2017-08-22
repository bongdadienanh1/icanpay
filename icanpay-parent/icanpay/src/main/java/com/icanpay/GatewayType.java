package com.icanpay;

public enum GatewayType {
	/**
	 * 未知网关类型
	 */
	None,

	/**
	 * 支付宝
	 */
	Alipay,

	/**
	 * 微信支付
	 */
	WeChatPayment,

	/**
	 * 中国银联
	 */
	UnionPay,

	/**
	 * 财付通
	 */
	Tenpay,

	/**
	 * PayPal
	 */
	PayPal,
}
