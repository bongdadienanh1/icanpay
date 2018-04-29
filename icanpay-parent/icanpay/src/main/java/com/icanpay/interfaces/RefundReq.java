package com.icanpay.interfaces;

import com.alipay.api.AlipayApiException;
import com.icanpay.Refund;

public interface RefundReq {
	/**
	 * 创建退款
	 * 
	 * @param refund
	 * @return
	 * @throws AlipayApiException
	 * @throws Exception
	 */
	Refund buildRefund(Refund refund);

	/**
	 * 查询退款结果
	 * 
	 * @param refund
	 * @return
	 * @throws AlipayApiException
	 * @throws Exception
	 */
	Refund buildRefundQuery(Refund refund);
}
