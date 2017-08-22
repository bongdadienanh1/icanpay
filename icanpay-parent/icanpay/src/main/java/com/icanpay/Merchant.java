package com.icanpay;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;

/**
 * 商户数据
 * 
 * @author milanyangbo
 *
 */
public class Merchant {

	String partner;
	String key;
	String email;
	String appId;
	URI notifyUrl;
	URI returnUrl;
	GatewayType gatewayType;

	public Merchant() {
	}

	public Merchant(String userName, String key, URI notifyUrl,
			GatewayType gatewayType) {
		this.partner = userName;
		this.key = key;
		this.notifyUrl = notifyUrl;
		this.gatewayType = gatewayType;
	}

	public String getPartner() {
		if (StringUtils.isBlank(partner)) {
			throw new IllegalArgumentException("Partner-商户帐号没有设置");
		}
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public URI getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(URI notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public URI getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(URI returnUrl) {
		this.returnUrl = returnUrl;
	}

	public GatewayType getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(GatewayType gatewayType) {
		this.gatewayType = gatewayType;
	}
}
