package com.icanpay.config;

import com.icanpay.exceptions.GatewayException;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.providers.UnionpayGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class UnionpayProperties extends BasePayProperties {

	@Value("${unionpay.merid:}")
	private String merid;
	@Value("${unionpay.notifyurl:}")
	private String notifyurl;
	@Value("${unionpay.returnurl:}")
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
		UnionpayGateway unionpayGateway = new UnionpayGateway();
		unionpayGateway.getMerchant().setPartner(this.getMerid());
		try {
			unionpayGateway.getMerchant().setNotifyUrl(new URI(this.getNotifyurl()));
			unionpayGateway.getMerchant().setReturnUrl(new URI(this.getReturnurl()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			throw new GatewayException(e.getMessage(), e);
		}
		return unionpayGateway;
	}
}
