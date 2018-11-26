package com.icanpay.interfaces;

/**
 * 支付订单通过form表单提交的HTML代码
 * 
 * @author milanyangbo
 *
 */
public interface WapPaymentForm {

	/**
	 * 创建包含支付订单数据的form表单的HTML代码
	 * 
	 * @return
	 */
	String buildWapPaymentForm();
}
