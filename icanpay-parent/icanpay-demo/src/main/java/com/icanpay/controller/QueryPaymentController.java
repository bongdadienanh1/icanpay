package com.icanpay.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icanpay.PaymentSetting;
import com.icanpay.enums.GatewayType;
import com.icanpay.properties.AlipayProperties;
import com.icanpay.properties.UnionPayProperties;
import com.icanpay.properties.WeChatPaymentProperties;
import com.unionpay.acp.sdk.SDKConfig;

@RestController
@RequestMapping("/querypayment")
public class QueryPaymentController {

	@Autowired
	private AlipayProperties alipayProperties;

	@Autowired
	private WeChatPaymentProperties weChatPaymentProperties;

	@Autowired
	private UnionPayProperties unionPayProperties;

	@GetMapping("/query")
	public void QueryPayment(Integer type) throws Exception {

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
			querySetting.getMerchant().setNotifyUrl(
					new URI(alipayProperties.getNotifyurl()));
			querySetting.getMerchant().setReturnUrl(
					new URI(alipayProperties.getReturnurl()));

		}

		if (gatewayType == GatewayType.WeChatPayment) {
			querySetting.getMerchant().setAppId(
					weChatPaymentProperties.getAppid());
			querySetting.getMerchant().setPartner(
					weChatPaymentProperties.getMch_id());
			querySetting.getMerchant().setKey(weChatPaymentProperties.getKey());
			querySetting.getMerchant().setNotifyUrl(
					new URI(weChatPaymentProperties.getNotifyurl()));
			querySetting.getMerchant().setReturnUrl(
					new URI(weChatPaymentProperties.getReturnurl()));
		}

		if (gatewayType == GatewayType.UnionPay) {
			querySetting.getMerchant()
					.setPartner(unionPayProperties.getMerid());
			querySetting.getMerchant().setNotifyUrl(
					new URI(unionPayProperties.getNotifyurl()));
			querySetting.getMerchant().setReturnUrl(
					new URI(unionPayProperties.getReturnurl()));
		}

		// 查询时需要设置订单的Id与金额，在查询结果中将会核对订单的Id与金额，如果不相符会返回查询失败。
		querySetting.getOrder().setOrderAmount(0.01);
		querySetting.getOrder().setOrderNo("yourorderno");

		if (querySetting.isCanQueryNow() && querySetting.queryNow()) {
			// 订单已支付
		}
	}
}
