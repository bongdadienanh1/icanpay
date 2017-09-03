package com.icanpay.enums;

public enum GatewayType {
	/**
	 * 未知网关类型
	 */
	None(0),

	/**
	 * 支付宝
	 */
	Alipay(1),

	/**
	 * 微信支付
	 */
	WeChatPayment(2),

	/**
	 * 中国银联
	 */
	UnionPay(3),

	/**
	 * 财付通
	 */
	Tenpay(4),

	/**
	 * PayPal
	 */
	PayPal(5), ;

	private Integer code;

	GatewayType(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}
