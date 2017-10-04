package com.icanpay.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.icanpay.Merchant;
import com.icanpay.enums.GatewayType;
import com.icanpay.enums.PaymentNotifyMethod;
import com.icanpay.events.PaymentFailedEventArgs;
import com.icanpay.events.PaymentFailedListener;
import com.icanpay.events.PaymentNotify;
import com.icanpay.events.PaymentSucceedEventArgs;
import com.icanpay.events.PaymentSucceedListener;
import com.icanpay.events.UnknownGatewayEventArgs;
import com.icanpay.events.UnknownGatewayListener;
import com.icanpay.properties.AlipayProperties;
import com.icanpay.properties.UnionPayProperties;
import com.icanpay.properties.WeChatPaymentProperties;

//@RestController
//@RequestMapping("/notify")
public class NotifyController {

	List<Merchant> merchantList;

	PaymentNotify notify;

	@Autowired
	private AlipayProperties alipayProperties;

	@Autowired
	private WeChatPaymentProperties weChatPaymentProperties;

	@Autowired
	private UnionPayProperties unionPayProperties;

	public NotifyController() {

		merchantList = new ArrayList<Merchant>();

		Merchant alipayMerchant = new Merchant();
		alipayMerchant.setGatewayType(GatewayType.Alipay);
		alipayMerchant.setAppId(alipayProperties.getAppid());
		alipayMerchant.setEmail(alipayProperties.getSeller_email());
		alipayMerchant.setPartner(alipayProperties.getPartner());
		alipayMerchant.setKey(alipayProperties.getKey());
		alipayMerchant.setPrivateKeyPem(alipayProperties.getPrivatekeypem());
		alipayMerchant.setPublicKeyPem(alipayProperties.getPublicKeypem());

		Merchant weChatPaymentMerchant = new Merchant();
		weChatPaymentMerchant.setGatewayType(GatewayType.WeChatPayment);
		weChatPaymentMerchant.setAppId(weChatPaymentProperties.getAppid());
		weChatPaymentMerchant.setPartner(weChatPaymentProperties.getMch_id());
		weChatPaymentMerchant.setKey(weChatPaymentProperties.getKey());

		Merchant unionPayMerchant = new Merchant();
		unionPayMerchant.setGatewayType(GatewayType.UnionPay);
		unionPayMerchant.setPartner(unionPayProperties.getMerid());

		merchantList.add(alipayMerchant);
		merchantList.add(unionPayMerchant);
		merchantList.add(weChatPaymentMerchant);

		// 添加到商户数据集合
		notify = new PaymentNotify(merchantList);

		notify.setPaymentSucceed(new PaymentSucceedListener() {

			@Override
			public void handleEvent(PaymentSucceedEventArgs event) {
				// TODO Auto-generated method stub
				// 支付成功时时的处理代码
				if (event.getPaymentNotifyMethod() == PaymentNotifyMethod.AutoReturn) {
					// 当前是用户的浏览器自动返回时显示充值成功页面
				} else {
					// 支付结果的发送方式，以服务端接收为准
				}
			}

		});

		notify.setPaymentFailed(new PaymentFailedListener() {

			@Override
			public void handleEvent(PaymentFailedEventArgs event) {
				// TODO Auto-generated method stub

			}

		});

		notify.setUnknownGateway(new UnknownGatewayListener() {

			@Override
			public void handleEvent(UnknownGatewayEventArgs event) {
				// TODO Auto-generated method stub

			}
		});

	}

	// @RequestMapping("/servernotify")
	public void ServerNotify() throws Exception {
		// 接收并处理支付通知
		notify.received(PaymentNotifyMethod.ServerNotify);
	}

	// @RequestMapping("/autoreturn")
	public void AutoReturn() throws Exception {
		// 接收并处理支付通知
		notify.received(PaymentNotifyMethod.AutoReturn);
	}
}
