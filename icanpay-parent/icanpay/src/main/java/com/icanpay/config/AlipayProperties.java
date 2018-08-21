package com.icanpay.config;

import com.icanpay.exceptions.GatewayException;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.providers.AlipayGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class AlipayProperties extends BasePayProperties {
	@Value("${alipay.appid:}")
	private String appid;
	@Value("${alipay.partner:}")
	private String partner;
	@Value("${alipay.seller_email:}")
	private String seller_email;
	@Value("${alipay.key:}")
	private String key;
	@Value("${alipay.privatekeypem:}")
	private String privatekeypem;
	@Value("${alipay.publicKeypem:}")
	private String publicKeypem;
	@Value("${alipay.notifyurl:}")
	private String notifyurl;
	@Value("${alipay.returnurl:}")
	private String returnurl;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public String getPrivatekeypem() {
		return privatekeypem;
	}

	public void setPrivatekeypem(String privatekeypem) {
		this.privatekeypem = privatekeypem;
	}

	public String getPublicKeypem() {
		return publicKeypem;
	}

	public void setPublicKeypem(String publicKeypem) {
		this.publicKeypem = publicKeypem;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	@Override
	GatewayBase initGateway() {
		AlipayGateway alipayGateway = new AlipayGateway();
		alipayGateway.getMerchant().setAppId(this.getAppid());
		alipayGateway.getMerchant().setEmail(this.getSeller_email());
		alipayGateway.getMerchant().setPartner(this.getPartner());
		alipayGateway.getMerchant().setKey(this.getKey());
		alipayGateway.getMerchant().setPrivateKeyPem(this.getPrivatekeypem());
		alipayGateway.getMerchant().setPublicKeyPem(this.getPublicKeypem());
		try {
			alipayGateway.getMerchant().setNotifyUrl(new URI(this.getNotifyurl()));
			alipayGateway.getMerchant().setReturnUrl(new URI(this.getReturnurl()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			throw new GatewayException(e.getMessage(), e);
		}
		return alipayGateway;
	}
}
