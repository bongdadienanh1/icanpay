package com.icanpay.interfaces;

/**
 * 向支付网关查询url发送需要查询的订单数据，支付网关在查询url页面输出查询结果
 * 
 * @author milanyangbo
 *
 */
public interface QueryNow {
	/**
	 * 查询订单是否支付成功。
	 * @return
	 */
	boolean queryNow();

}
