package com.icanpay.events;

import java.util.ArrayList;
import java.util.List;

import com.icanpay.GatewayBase;
import com.icanpay.Merchant;
import com.icanpay.NotifyProcess;
import com.icanpay.enums.GatewayType;
import com.icanpay.enums.PaymentNotifyMethod;

/**
 * 网关返回的支付通知数据的接受
 * 
 * @author milanyangbo
 *
 */
public class PaymentNotify {

	List<Merchant> merchantList;

	public PaymentNotify() {
		this(new ArrayList<Merchant>());
	}

	public PaymentNotify(Merchant merchant) {
		merchantList = new ArrayList<Merchant>();
	}

	public PaymentNotify(List<Merchant> merchantList) {
		this.merchantList = merchantList;
	}

	/**
	 * 网关返回的支付通知验证失败时触发
	 */
	PaymentFailedListener PaymentFailed;

	/**
	 * 网关返回的支付通知验证成功时触发
	 */
	PaymentSucceedListener PaymentSucceed;

	/**
	 * 返回通知消息的网关无法识别时触发
	 */
	UnknownGatewayListener UnknownGateway;

	public void setPaymentFailed(PaymentFailedListener paymentFailed) {
		PaymentFailed = paymentFailed;
	}

	public void setPaymentSucceed(PaymentSucceedListener paymentSucceed) {
		PaymentSucceed = paymentSucceed;
	}

	public void setUnknownGateway(UnknownGatewayListener unknownGateway) {
		UnknownGateway = unknownGateway;
	}

	protected void onPaymentFailed(PaymentFailedEventArgs e) {
		if (PaymentFailed != null) {
			PaymentFailed.handleEvent(e);
		}
	}

	protected void onPaymentSucceed(PaymentSucceedEventArgs e) {
		if (PaymentSucceed != null) {
			PaymentSucceed.handleEvent(e);
		}
	}

	protected void onUnknownGateway(UnknownGatewayEventArgs e) {
		if (UnknownGateway != null) {
			UnknownGateway.handleEvent(e);
		}
	}

	/**
	 * 接收并验证网关的支付通知
	 * 
	 * @param paymentNotifyMethod
	 * @throws Exception
	 */
	public void received(PaymentNotifyMethod paymentNotifyMethod)
			throws Exception {
		GatewayBase gateway = NotifyProcess.getGateway();
		gateway.setPaymentNotifyMethod(paymentNotifyMethod);
		if (gateway.getGatewayType() != GatewayType.None) {
			gateway.setMerchant(getMerchant(gateway.getGatewayType()));
			if (gateway.validateNotify()) {
				onPaymentSucceed(new PaymentSucceedEventArgs(gateway));
				gateway.writeSucceedFlag();
			} else {
				onPaymentFailed(new PaymentFailedEventArgs(gateway));
			}
		} else {
			gateway.setPaymentNotifyMethod(PaymentNotifyMethod.None);
			onUnknownGateway(new UnknownGatewayEventArgs(gateway));
		}
	}

	/**
	 * 添加商户数据。与添加的商户数据重复的网关将会被删除
	 * 
	 * @param merchant
	 *            商户数据
	 */
	public void addMerchant(Merchant merchant) {
		removeMerchant(merchant.getGatewayType());
		merchantList.add(merchant);
	}

	/**
	 * 获得商户数据。网关存在多个商户数据时返回第一个，无法找到返回null
	 * 
	 * @param gatewayType
	 *            网关类型
	 * @return 网关存在多个商户数据时返回第一个，无法找到返回null
	 */
	public Merchant getMerchant(GatewayType gatewayType) {
		return merchantList.stream()
				.filter(m -> m.getGatewayType() == gatewayType).findFirst()
				.get();
	}

	/**
	 * 删除商户数据
	 * 
	 * @param gatewayType
	 *            网关类型
	 */
	public void removeMerchant(GatewayType gatewayType) {
		Merchant removeMerchant = merchantList.stream()
				.filter(m -> m.getGatewayType() == gatewayType).findFirst()
				.get();
		if (removeMerchant != null) {
			merchantList.remove(removeMerchant);
		}
	}

}
