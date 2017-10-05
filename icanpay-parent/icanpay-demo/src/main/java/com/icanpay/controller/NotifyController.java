package com.icanpay.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.unionpay.acp.sdk.SDKConfig;

@RestController
@RequestMapping("/notify")
public class NotifyController {

	List<Merchant> merchantList;

	PaymentNotify notify;

	private AlipayProperties alipayProperties;

	private WeChatPaymentProperties weChatPaymentProperties;

	private UnionPayProperties unionPayProperties;

	@Autowired
	public NotifyController(AlipayProperties alipayProperties,
			WeChatPaymentProperties weChatPaymentProperties,
			UnionPayProperties unionPayProperties) {
		this.alipayProperties = alipayProperties;
		this.weChatPaymentProperties = weChatPaymentProperties;
		this.unionPayProperties = unionPayProperties;

		// 从应用的classpath下加载acp_sdk.properties属性文件并将该属性文件中的键值对赋值到SDKConfig类中
		SDKConfig.getConfig().loadPropertiesFromSrc();

		merchantList = new ArrayList<Merchant>();

		Merchant alipayMerchant = new Merchant();
		alipayMerchant.setGatewayType(GatewayType.Alipay);
		alipayMerchant.setAppId(this.alipayProperties.getAppid());
		alipayMerchant.setEmail(this.alipayProperties.getSeller_email());
		alipayMerchant.setPartner(this.alipayProperties.getPartner());
		alipayMerchant.setKey(this.alipayProperties.getKey());
		alipayMerchant.setPrivateKeyPem(this.alipayProperties
				.getPrivatekeypem());
		alipayMerchant.setPublicKeyPem(this.alipayProperties.getPublicKeypem());

		Merchant weChatPaymentMerchant = new Merchant();
		weChatPaymentMerchant.setGatewayType(GatewayType.WeChatPayment);
		weChatPaymentMerchant.setAppId(this.weChatPaymentProperties.getAppid());
		weChatPaymentMerchant.setPartner(this.weChatPaymentProperties
				.getMch_id());
		weChatPaymentMerchant.setKey(this.weChatPaymentProperties.getKey());

		Merchant unionPayMerchant = new Merchant();
		unionPayMerchant.setGatewayType(GatewayType.UnionPay);
		unionPayMerchant.setPartner(this.unionPayProperties.getMerid());

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

	@GetMapping("/servernotify")
	public void ServerNotify() throws Exception {
		// 接收并处理支付通知
		notify.received(PaymentNotifyMethod.ServerNotify);
	}

	@GetMapping("/autoreturn")
	public void AutoReturn() throws Exception {
		// 接收并处理支付通知
		notify.received(PaymentNotifyMethod.AutoReturn);
	}
}
