package com.icanpay.ioc;

import com.icanpay.events.PaymentNotify;
import com.icanpay.gateways.Gateways;
import com.icanpay.gateways.GatewaysImpl;
import com.icanpay.properties.AlipayProperties;
import com.icanpay.properties.UnionPayProperties;
import com.icanpay.properties.WeChatPaymentProperties;
import com.icanpay.providers.AlipayGateway;
import com.icanpay.providers.UnionPayGateway;
import com.icanpay.providers.WeChatPayGataway;
import com.unionpay.acp.sdk.SDKConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class ICanpayConfig {

	static Logger logger = LoggerFactory.getLogger(ICanpayConfig.class);

	static {
		SDKConfig.getConfig().loadPropertiesFromSrc();
	}

	@Autowired
	private AlipayProperties alipayProperties;

	@Autowired
	private WeChatPaymentProperties weChatPaymentProperties;

	@Autowired
	private UnionPayProperties unionPayProperties;

	@Bean()
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Gateways gateways() {

		Gateways gateways = new GatewaysImpl();
		AlipayGateway alipayGateway = new AlipayGateway();
		alipayGateway.getMerchant().setAppId(alipayProperties.getAppid());
		alipayGateway.getMerchant().setEmail(alipayProperties.getSeller_email());
		alipayGateway.getMerchant().setPartner(alipayProperties.getPartner());
		alipayGateway.getMerchant().setKey(alipayProperties.getKey());
		alipayGateway.getMerchant().setPrivateKeyPem(alipayProperties.getPrivatekeypem());
		alipayGateway.getMerchant().setPublicKeyPem(alipayProperties.getPublicKeypem());
		try {
			alipayGateway.getMerchant().setNotifyUrl(new URI(alipayProperties.getNotifyurl()));
			alipayGateway.getMerchant().setReturnUrl(new URI(alipayProperties.getReturnurl()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
		gateways.add(alipayGateway);

		WeChatPayGataway weChatPayGataway = new WeChatPayGataway();
		weChatPayGataway.getMerchant().setAppId(weChatPaymentProperties.getAppid());
		weChatPayGataway.getMerchant().setPartner(weChatPaymentProperties.getMch_id());
		weChatPayGataway.getMerchant().setKey(weChatPaymentProperties.getKey());
		try {
			weChatPayGataway.getMerchant().setNotifyUrl(new URI(weChatPaymentProperties.getNotifyurl()));
			weChatPayGataway.getMerchant().setReturnUrl(new URI(weChatPaymentProperties.getReturnurl()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
		gateways.add(weChatPayGataway);

		UnionPayGateway unionPayGateway = new UnionPayGateway();
		unionPayGateway.getMerchant().setPartner(unionPayProperties.getMerid());
		try {
			unionPayGateway.getMerchant().setNotifyUrl(new URI(unionPayProperties.getNotifyurl()));
			unionPayGateway.getMerchant().setReturnUrl(new URI(unionPayProperties.getReturnurl()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
		gateways.add(unionPayGateway);
		return gateways;

	}

	@Bean()
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public PaymentNotify paymentNotify(@Autowired Gateways gateways) {
		return new PaymentNotify(gateways.getMerchants());
	}
}
