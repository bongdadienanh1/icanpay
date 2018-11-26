package com.icanpay.notifys;

import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.GatewayParameter;
import com.icanpay.providers.WeChatpayGataway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeChatpayNotifyVerify extends BaseNotifyVerify {
	static final String[] weChatpayGatewayVerifyParmaNames = {"return_code", "appid", "mch_id", "nonce_str", "result_code"};

	@Override
	public GatewayBase verify(List<GatewayParameter> gatewayParameterData) {
		if (isWeChatpayGateway(gatewayParameterData)) {
			return new WeChatpayGataway(gatewayParameterData);
		}
		return null;
	}

	/**
	 * 是否是微信支付网关
	 *
	 * @param gatewayParameterData
	 * @return
	 */
	private boolean isWeChatpayGateway(List<GatewayParameter> gatewayParameterData) {
		// TODO Auto-generated method stub
		return existParameter(weChatpayGatewayVerifyParmaNames, gatewayParameterData);
	}
}
