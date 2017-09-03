package com.icanpay.interfaces;

/**
 * 支付订单通过form表单提交的HTML代码
 * 
 * @author milanyangbo
 *
 */
public interface PaymentForm {

	/**
	 * 创建包含支付订单数据的form表单的HTML代码
	 * 
	 * @return
	 * @throws Exception
	 */
	String buildPaymentForm() throws Exception;
}
