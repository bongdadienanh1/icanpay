package com.icanpay.demo;

import com.icanpay.enums.GatewayType;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.GatewayParameter;
import com.icanpay.interfaces.PaymentApp;
import com.icanpay.interfaces.PaymentForm;
import com.icanpay.interfaces.QueryNow;
import com.icanpay.interfaces.WapPaymentUrl;

import java.util.List;
import java.util.Map;

public class DemoGateway extends GatewayBase implements PaymentForm, WapPaymentUrl, PaymentApp, QueryNow {

	public DemoGateway() {

	}

	public DemoGateway(List<GatewayParameter> gatewayParameterData) {
		super(gatewayParameterData);
	}

	@Override
	public GatewayType getGatewayType() {
		return GatewayType.Demo;
	}

	@Override
	protected boolean checkNotifyData() {
		return false;
	}

	@Override
	public void writeSucceedFlag() {

	}

	@Override
	public Map<String, String> buildPayParams() {
		return null;
	}

	@Override
	public String buildPaymentForm() {
		return null;
	}

	@Override
	public boolean queryNow() {
		return false;
	}

	@Override
	public String buildWapPaymentUrl(Map<String, String> map) {
		return null;
	}
}
