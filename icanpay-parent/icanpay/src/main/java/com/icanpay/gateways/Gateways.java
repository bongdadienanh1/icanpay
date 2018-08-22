package com.icanpay.gateways;

import com.icanpay.Merchant;
import com.icanpay.enums.GatewayTradeType;
import com.icanpay.enums.GatewayType;
import com.icanpay.exceptions.GatewayException;

import java.util.List;

public interface Gateways {

	/**
	 * 添加网关
	 * 
	 * @param gateway
	 * @return
	 * @throws GatewayException
	 */
	boolean add(GatewayBase gateway);

	/**
	 * 通过网关类型获取网关
	 * 
	 * @param gatewayType
	 * @return
	 * @throws GatewayException
	 */
	GatewayBase get(GatewayType gatewayType);

	/**
	 * 通过网关类型,交易类型获取网关
	 * 
	 * @param gatewayType
	 * @param gatewayTradeType
	 * @return
	 * @throws GatewayException
	 */
	GatewayBase get(GatewayType gatewayType, GatewayTradeType gatewayTradeType);

	/**
	 * 获取网关列表
	 * 
	 * @return
	 */
	List<GatewayBase> getList();

	/**
	 * 商户信息
	 * 
	 * @return
	 */
	List<Merchant> getMerchants();

}
