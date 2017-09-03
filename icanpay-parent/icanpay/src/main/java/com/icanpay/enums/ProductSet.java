package com.icanpay.enums;

/**
 * 支付类型
 * 
 * @author milanyangbo
 *
 */
public enum ProductSet {
	Web(0), Wap(1), APP(2), ;

	private Integer code;

	ProductSet(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}
