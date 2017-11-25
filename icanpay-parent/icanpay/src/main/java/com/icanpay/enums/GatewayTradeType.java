package com.icanpay.enums;

/**
 * 网关的交易类型
 * 
 * @author milanyangbo
 *
 */
public enum GatewayTradeType {

	/**
	 * 电脑网站支付
	 */
	Web(0),

	/**
	 * 手机网站支付
	 */
	Wap(1),

	/**
	 * App支付
	 */
	APP(2),

	/**
	 * 扫码支付
	 */
	Scan(3),

	/**
	 * 公众号支付
	 */
	Public(4),

	/**
	 * 条码支付
	 */
	Barcode(5),

	/**
	 * 小程序支付
	 */
	Applet(6), ;

	private Integer code;

	GatewayTradeType(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}
