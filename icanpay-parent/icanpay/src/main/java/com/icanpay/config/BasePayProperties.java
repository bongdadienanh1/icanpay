package com.icanpay.config;

import com.icanpay.gateways.GatewayBase;

public abstract class BasePayProperties {
	public abstract GatewayBase initGateway();
}
