package com.icanpay.demo;


import com.icanpay.config.BasePayProperties;
import com.icanpay.exceptions.GatewayException;
import com.icanpay.gateways.GatewayBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class DemoProperties extends BasePayProperties {
	@Value("${demo.merid:}")
	private String merid;
	@Value("${demo.notifyurl:}")
	private String notifyurl;
	@Value("${demo.returnurl:}")
	private String returnurl;


	public String getMerid() {
		return merid;
	}

	public void setMerid(String merid) {
		this.merid = merid;
	}

	public String getNotifyurl() {
		return notifyurl;
	}

	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

	public String getReturnurl() {
		return returnurl;
	}

	public void setReturnurl(String returnurl) {
		this.returnurl = returnurl;
	}

	@Override

	public GatewayBase initGateway() {
		DemoGateway demoGateway = new DemoGateway();
		demoGateway.getMerchant().setPartner(this.getMerid());
		try {
			demoGateway.getMerchant().setNotifyUrl(new URI(this.getNotifyurl()));
			demoGateway.getMerchant().setReturnUrl(new URI(this.getReturnurl()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			throw new GatewayException(e.getMessage(), e);
		}
		return demoGateway;
	}
}
