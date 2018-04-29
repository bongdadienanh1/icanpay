package com.icanpay.interfaces;

/**
 * 订单是使用二维码支付时创建订单的支付二维码
 * 
 * @author milanyangbo
 *
 */
public interface PaymentQRCode {

	/**
	 * 获得订单的支付二维码内容
	 * 
	 * @return 订单的支付二维码内容
	 * @throws Exception
	 */
	String getPaymentQRCodeContent();
}
