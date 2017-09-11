package com.icanpay.providers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.icanpay.GatewayBase;
import com.icanpay.GatewayParameter;
import com.icanpay.enums.GatewayType;

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

	public GatewayType getGatewayType() {
		return GatewayType.None;
	}

	@Override
	protected boolean checkNotifyData(HttpServletRequest req) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String writeSucceedFlag() {
		// TODO Auto-generated method stub
		return "";
	}

}
