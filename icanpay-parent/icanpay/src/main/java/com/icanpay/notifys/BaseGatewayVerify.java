package com.icanpay.notifys;

import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.GatewayParameter;

import java.util.List;

public abstract class BaseGatewayVerify {

	abstract GatewayBase verify(List<GatewayParameter> gatewayParameterData);

	/**
	 * 网关参数数据项中是否存在指定的所有参数名
	 *
	 * @param parmaName
	 *            参数名数组
	 * @param gatewayParameterData
	 *            数据项
	 * @return
	 */
	protected boolean existParameter(String[] parmaName, List<GatewayParameter> gatewayParameterData) {
		// TODO Auto-generated method stub
		int compareCount = 0;
		for (String item : parmaName) {
			GatewayParameter existsParam = gatewayParameterData.stream().filter(p -> p.getName().equals(item)).findFirst().orElse(null);

			if (existsParam != null) {
				compareCount++;
			}
		}

		if (compareCount == parmaName.length) {
			return true;
		}

		return false;
	}
}
