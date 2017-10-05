package com.icanpay.controller;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

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
@RequestMapping("/webpayment")
public class WebPaymentController {

	@Autowired
	private AlipayProperties alipayProperties;

	@Autowired
	private WeChatPaymentProperties weChatPaymentProperties;

	@Autowired
	private UnionPayProperties unionPayProperties;

	@GetMapping("/alipay")
	public void Alipay() throws IOException, Exception {

		PaymentSetting paymentSetting = new PaymentSetting(GatewayType.Alipay);
		paymentSetting.getMerchant().setAppId(alipayProperties.getAppid());
		paymentSetting.getMerchant().setEmail(
				alipayProperties.getSeller_email());
		paymentSetting.getMerchant().setPartner(alipayProperties.getPartner());
		paymentSetting.getMerchant().setKey(alipayProperties.getKey());
		paymentSetting.getMerchant().setPrivateKeyPem(
				alipayProperties.getPrivatekeypem());
		paymentSetting.getMerchant().setPublicKeyPem(
				alipayProperties.getPublicKeypem());
		paymentSetting.getMerchant().setNotifyUrl(
				new URI(alipayProperties.getNotifyurl()));
		paymentSetting.getMerchant().setReturnUrl(
				new URI(alipayProperties.getReturnurl()));

		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo(
				new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		paymentSetting.getOrder().setSubject("alipay");
		paymentSetting.payment();
	}

	@GetMapping("/wechatpayment")
	public void WeChatPayment() throws IOException, Exception {

		PaymentSetting paymentSetting = new PaymentSetting(
				GatewayType.WeChatPayment);

		paymentSetting.getMerchant().setAppId(
				weChatPaymentProperties.getAppid());
		paymentSetting.getMerchant().setPartner(
				weChatPaymentProperties.getMch_id());
		paymentSetting.getMerchant().setKey(weChatPaymentProperties.getKey());
		paymentSetting.getMerchant().setNotifyUrl(
				new URI(weChatPaymentProperties.getNotifyurl()));

		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo(
				new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		paymentSetting.getOrder().setSubject("wechatpayment");
		paymentSetting.payment();
	}

	@GetMapping("/unionpay")
	public void UnionPay() throws IOException, Exception {
		// 从应用的classpath下加载acp_sdk.properties属性文件并将该属性文件中的键值对赋值到SDKConfig类中
		SDKConfig.getConfig().loadPropertiesFromSrc();

		PaymentSetting paymentSetting = new PaymentSetting(GatewayType.UnionPay);

		paymentSetting.getMerchant().setPartner(unionPayProperties.getMerid());
		paymentSetting.getMerchant().setNotifyUrl(
				new URI(unionPayProperties.getNotifyurl()));
		paymentSetting.getMerchant().setReturnUrl(
				new URI(unionPayProperties.getReturnurl()));

		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo(
				new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		paymentSetting.getOrder().setPaymentDate(new Date());
		paymentSetting.getOrder().setSubject("unionpay");
		paymentSetting.payment();
	}
}
