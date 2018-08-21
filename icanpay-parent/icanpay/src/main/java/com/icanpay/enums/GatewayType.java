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
	WeChatpay(2),

	/**
	 * 中国银联
	 */
	Unionpay(3),

	/**
	 * 财付通
	 */
	Tenpay(4),

	/**
	 * PayPal
	 */
	PayPal(5),;

	private int code;

	GatewayType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static GatewayType getGatewayType(int code) {
		for (GatewayType gatewayType : values()) {
			if (gatewayType.getCode() == code) {
				return gatewayType;
			}
		}
		return null;
	}
}
