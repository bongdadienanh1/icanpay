package com.icanpay;

import java.util.Date;

import com.icanpay.utils.Utility;

public class Refund {

	double orderAmount;
	double refundAmount;
	String orderNo;
	String tradeNo;
	String refoundNo;
	String refundDes;
	String refoundId;
	Date paymentDate;
	boolean status;

	public double getOrderAmount() {
		if (orderAmount < 0.01) {
			throw new IllegalArgumentException("OrderAmount-订单金额没有设置");
		}
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		if (orderAmount < 0.01) {
			throw new IllegalArgumentException("OrderAmount-订单金额必须大于或等于0.01");
		}
		this.orderAmount = orderAmount;
	}

	public double getRefundAmount() {
		if (refundAmount < 0.01) {
			throw new IllegalArgumentException("RefundAmount-退款金额没有设置");
		}
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		if (refundAmount < 0.01) {
			throw new IllegalArgumentException("RefundAmount-订单金额必须大于或等于0.01");
		}
		this.refundAmount = refundAmount;
	}

	public String getOrderNo() {
		if (Utility.isBlankOrEmpty(orderNo)) {
			throw new IllegalArgumentException("OrderNo-订单订单编号没有设置");
		}
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		if (Utility.isBlankOrEmpty(orderNo)) {
			throw new IllegalArgumentException("OrderNo-订单订单编号不能为空");
		}
		this.orderNo = orderNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getRefoundNo() {
		if (Utility.isBlankOrEmpty(refoundNo)) {
			throw new IllegalArgumentException("RefoundNo-商户退款单号没有设置");
		}
		return refoundNo;
	}

	public void setRefoundNo(String refoundNo) {
		if (Utility.isBlankOrEmpty(refoundNo)) {
			throw new IllegalArgumentException("RefoundNo-商户退款单号不能为空");
		}
		this.refoundNo = refoundNo;
	}

	public String getRefundDes() {
		return refundDes;
	}

	public void setRefundDes(String refundDes) {
		this.refundDes = refundDes;
	}

	public String getRefoundId() {
		return refoundId;
	}

	public void setRefoundId(String refoundId) {
		this.refoundId = refoundId;
	}

	public Date getPaymentDate() {
		if (paymentDate == null) {
			throw new IllegalArgumentException("PaymentDate-订单创建时间未赋值");
		}
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		if (paymentDate == null) {
			throw new IllegalArgumentException("PaymentDate-订单创建时间未赋值");
		}
		this.paymentDate = paymentDate;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
