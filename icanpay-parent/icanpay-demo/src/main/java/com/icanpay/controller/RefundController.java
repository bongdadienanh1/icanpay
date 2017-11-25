package com.icanpay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icanpay.PaymentSetting;
import com.icanpay.Refund;
import com.icanpay.enums.GatewayType;
import com.icanpay.properties.AlipayProperties;
import com.icanpay.properties.UnionPayProperties;
import com.icanpay.properties.WeChatPaymentProperties;
import com.unionpay.acp.sdk.SDKConfig;

@RestController
@RequestMapping("/refund")
public class RefundController {

	@Autowired
	private AlipayProperties alipayProperties;

	@Autowired
	private WeChatPaymentProperties weChatPaymentProperties;

	@Autowired
	private UnionPayProperties unionPayProperties;

	@GetMapping("/refund")
	public void Refund(Integer type) throws Exception {

		GatewayType gatewayType = GatewayType.Alipay;
		if (type == 0) {
			gatewayType = GatewayType.Alipay;
		}
		if (type == 1) {
			gatewayType = GatewayType.WeChatPayment;
		}
		if (type == 2) {
			// 从应用的classpath下加载acp_sdk.properties属性文件并将该属性文件中的键值对赋值到SDKConfig类中
			SDKConfig.getConfig().loadPropertiesFromSrc();
			gatewayType = GatewayType.UnionPay;
		}

		PaymentSetting querySetting = new PaymentSetting(gatewayType);

		if (gatewayType == GatewayType.Alipay) {
			querySetting.getMerchant().setAppId(alipayProperties.getAppid());
			querySetting.getMerchant().setEmail(
					alipayProperties.getSeller_email());
			querySetting.getMerchant()
					.setPartner(alipayProperties.getPartner());
			querySetting.getMerchant().setKey(alipayProperties.getKey());
			querySetting.getMerchant().setPrivateKeyPem(
					alipayProperties.getPrivatekeypem());
			querySetting.getMerchant().setPublicKeyPem(
					alipayProperties.getPublicKeypem());

		}

		if (gatewayType == GatewayType.WeChatPayment) {
			querySetting.getMerchant().setAppId(
					weChatPaymentProperties.getAppid());
			querySetting.getMerchant().setPartner(
					weChatPaymentProperties.getMch_id());
			querySetting.getMerchant().setKey(weChatPaymentProperties.getKey());
		}

		if (gatewayType == GatewayType.UnionPay) {
			querySetting.getMerchant()
					.setPartner(unionPayProperties.getMerid());
		}

		Refund refund = new Refund();
		refund.setOutRefoundNo("0000000000");
		if (querySetting.isCanRefund()) {
			querySetting.buildRefund(refund);
			querySetting.buildRefundQuery(refund);
		}
	}
}
