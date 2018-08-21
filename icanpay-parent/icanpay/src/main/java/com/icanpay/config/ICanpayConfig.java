package com.icanpay.config;

import com.icanpay.events.PaymentNotify;
import com.icanpay.gateways.Gateways;
import com.icanpay.gateways.GatewaysImpl;
import com.icanpay.notifys.BaseGatewayVerify;
import com.icanpay.notifys.NotifyProcess;
import com.unionpay.acp.sdk.SDKConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Set;

@Configuration
public class ICanpayConfig {

	static {
		SDKConfig.getConfig().loadPropertiesFromSrc();
	}

	@Bean()
	@Scope("prototype")
	public Gateways gateways(Set<BasePayProperties> payProperties) {
		Gateways gateways = new GatewaysImpl();
		payProperties.forEach(p -> {
			gateways.add(p.initGateway());
		});
		return gateways;
	}

	@Bean()
	public NotifyProcess notifyProcess(Set<BaseGatewayVerify> gatewayVerify) {
		NotifyProcess notifyProcess = new NotifyProcess();
		notifyProcess.setBaseGatewayVerify(gatewayVerify);
		return notifyProcess;
	}

	@Bean()
	@Scope("prototype")
	public PaymentNotify paymentNotify(@Autowired Gateways gateways, @Autowired NotifyProcess notifyProcess) {
		PaymentNotify paymentNotify = new PaymentNotify();
		paymentNotify.setMerchantList(gateways.getMerchants());
		paymentNotify.setNotifyProcess(notifyProcess);
		return paymentNotify;
	}

}
