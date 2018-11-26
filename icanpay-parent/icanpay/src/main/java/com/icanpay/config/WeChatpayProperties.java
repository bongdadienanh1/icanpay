package com.icanpay.config;

import com.icanpay.exceptions.GatewayException;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.providers.WeChatpayGataway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class WeChatpayProperties extends BasePayProperties {
	@Value("${wechatpay.appid:}")
	private String appid;
	@Value("${wechatpay.mch_id:}")
	private String mch_id;
	@Value("${wechatpay.key:}")
	private String key;
	@Value("${wechatpay.notifyurl:}")
	private String notifyurl;
	@Value("${wechatpay.returnurl:}")
	private String returnurl;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
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

	@Override
	public GatewayBase initGateway() {
		WeChatpayGataway weChatpayGataway = new WeChatpayGataway();
		weChatpayGataway.getMerchant().setAppId(this.getAppid());
		weChatpayGataway.getMerchant().setPartner(this.getMch_id());
		weChatpayGataway.getMerchant().setKey(this.getKey());
		try {
			weChatpayGataway.getMerchant().setNotifyUrl(new URI(this.getNotifyurl()));
			weChatpayGataway.getMerchant().setReturnUrl(new URI(this.getReturnurl()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			throw new GatewayException(e.getMessage(), e);
		}
		return weChatpayGataway;
	}
}

