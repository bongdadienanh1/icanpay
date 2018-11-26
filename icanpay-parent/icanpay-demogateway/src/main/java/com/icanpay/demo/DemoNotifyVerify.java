package com.icanpay.demo;

import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.GatewayParameter;
import com.icanpay.notifys.BaseNotifyVerify;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoNotifyVerify extends BaseNotifyVerify {
	static final String[] demoGatewayVerifyParmaNames = {"arg1", "arg2"};

	@Override
	public GatewayBase verify(List<GatewayParameter> gatewayParameterData) {
		if (isDemoGateway(gatewayParameterData)) {
			return new DemoGateway(gatewayParameterData);
		}
		return null;
	}

	private boolean isDemoGateway(List<GatewayParameter> gatewayParameterData) {
		// TODO Auto-generated method stub
		return existParameter(demoGatewayVerifyParmaNames, gatewayParameterData);
	}
}
