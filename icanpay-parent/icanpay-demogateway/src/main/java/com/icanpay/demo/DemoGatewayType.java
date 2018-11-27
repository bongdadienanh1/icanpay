package com.icanpay.demo;

import com.icanpay.enums.GatewayType;

public enum DemoGatewayType implements GatewayType {

	Demo(6);

	private int code;

	DemoGatewayType(int code) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}

}
