package com.icanpay.notifys;

import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.GatewayParameter;
import com.icanpay.providers.AlipayGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlipayNotifyVerify extends BaseNotifyVerify {
	static final String[] alipayGatewayVerifyParmaNames = {"notify_type", "notify_id", "notify_time", "sign", "sign_type"};
	static final String[] alipayWapGatewayVerifyParmaNames = {"auth_app_id", "method", "seller_id", "sign", "sign_type"};

	@Override
	public GatewayBase verify(List<GatewayParameter> gatewayParameterData) {
		if (isAlipayGateway(gatewayParameterData)) {
			return new AlipayGateway(gatewayParameterData);
		}
		return null;
	}

	/**
	 * 是否是支付宝网关
	 *
	 * @param gatewayParameterData
	 * @return
	 */
	private boolean isAlipayGateway(List<GatewayParameter> gatewayParameterData) {
		// TODO Auto-generated method stub
		return existParameter(alipayGatewayVerifyParmaNames, gatewayParameterData) || existParameter(alipayWapGatewayVerifyParmaNames,
				gatewayParameterData);
	}
}
