package com.icanpay.providers;

import java.util.List;

import com.icanpay.enums.BasicGatewayType;
import com.icanpay.enums.GatewayType;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.GatewayParameter;

public class NullGateway extends GatewayBase {

	/**
	 * 初始化未知网关
	 */
	public NullGateway() {
	}

	/**
	 * 初始化未知网关
	 * 
	 * @param gatewayParameterData
	 *            网关通知的数据集合
	 */
	public NullGateway(List<GatewayParameter> gatewayParameterData) {
		super(gatewayParameterData);
	}

	@Override
	public GatewayType getGatewayType() {
		return BasicGatewayType.None;
	}

	@Override
	protected boolean checkNotifyData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeSucceedFlag() {
		// TODO Auto-generated method stub
	}

}
