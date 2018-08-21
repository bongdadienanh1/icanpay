package com.icanpay.notifys;

import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.GatewayParameter;
import com.icanpay.providers.UnionpayGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnionpayGatewayVerify extends BaseGatewayVerify {
	static final String[] unionpayGatewayVerifyParmaNames = {"respMsg", "merId", "respCode", "orderId", "queryId"};

	@Override
	GatewayBase verify(List<GatewayParameter> gatewayParameterData) {
		if (isUnionpayGateway(gatewayParameterData)) {
			return new UnionpayGateway(gatewayParameterData);
		}
		return null;
	}

	/**
	 * 是否是银联支付网关
	 *
	 * @param gatewayParameterData
	 * @return
	 */
	private boolean isUnionpayGateway(List<GatewayParameter> gatewayParameterData) {
		// TODO Auto-generated method stub
		return existParameter(unionpayGatewayVerifyParmaNames, gatewayParameterData);
	}
}
