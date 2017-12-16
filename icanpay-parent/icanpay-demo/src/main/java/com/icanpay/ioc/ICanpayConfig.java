package com.icanpay.ioc;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.icanpay.exceptions.GatewayException;
import com.icanpay.gateways.Gateways;
import com.icanpay.gateways.GatewaysImpl;
import com.icanpay.properties.AlipayProperties;
import com.icanpay.properties.UnionPayProperties;
import com.icanpay.properties.WeChatPaymentProperties;
import com.icanpay.providers.AlipayGateway;
import com.icanpay.providers.UnionPayGateway;
import com.icanpay.providers.WeChatPayGataway;

@Configuration
@Component
public class ICanpayConfig {

	@Autowired
	private AlipayProperties alipayProperties;

	@Autowired
	private WeChatPaymentProperties weChatPaymentProperties;

	@Autowired
	private UnionPayProperties unionPayProperties;

	@Bean("prototype")
	public Gateways gateways() throws GatewayException, URISyntaxException {

		Gateways gateways = new GatewaysImpl();
		AlipayGateway alipayGateway = new AlipayGateway();
		alipayGateway.getMerchant().setAppId(alipayProperties.getAppid());
		alipayGateway.getMerchant()
				.setEmail(alipayProperties.getSeller_email());
		alipayGateway.getMerchant().setPartner(alipayProperties.getPartner());
		alipayGateway.getMerchant().setKey(alipayProperties.getKey());
		alipayGateway.getMerchant().setPrivateKeyPem(
				alipayProperties.getPrivatekeypem());
		alipayGateway.getMerchant().setPublicKeyPem(
				alipayProperties.getPublicKeypem());
		alipayGateway.getMerchant().setNotifyUrl(
				new URI(alipayProperties.getNotifyurl()));
		alipayGateway.getMerchant().setReturnUrl(
				new URI(alipayProperties.getReturnurl()));
		gateways.add(alipayGateway);

		WeChatPayGataway weChatPayGataway = new WeChatPayGataway();
		weChatPayGataway.getMerchant().setAppId(
				weChatPaymentProperties.getAppid());
		weChatPayGataway.getMerchant().setPartner(
				weChatPaymentProperties.getMch_id());
		weChatPayGataway.getMerchant().setKey(weChatPaymentProperties.getKey());
		weChatPayGataway.getMerchant().setNotifyUrl(
				new URI(weChatPaymentProperties.getNotifyurl()));
		weChatPayGataway.getMerchant().setReturnUrl(
				new URI(weChatPaymentProperties.getReturnurl()));
		gateways.add(weChatPayGataway);

		UnionPayGateway unionPayGateway = new UnionPayGateway();
		unionPayGateway.getMerchant().setPartner(unionPayProperties.getMerid());
		unionPayGateway.getMerchant().setNotifyUrl(
				new URI(unionPayProperties.getNotifyurl()));
		unionPayGateway.getMerchant().setReturnUrl(
				new URI(unionPayProperties.getReturnurl()));
		gateways.add(unionPayGateway);
		return gateways;

	}
}
