package com.icanpay.gateways;

import java.util.List;

import com.icanpay.Merchant;
import com.icanpay.enums.GatewayTradeType;
import com.icanpay.enums.GatewayType;
import com.icanpay.exceptions.GatewayException;

public interface Gateways {

	/**
	 * 添加网关
	 * 
	 * @param gateway
	 * @return
	 * @throws GatewayException
	 */
	boolean add(GatewayBase gateway) throws GatewayException;

	/**
	 * 通过网关类型获取网关
	 * 
	 * @param gatewayType
	 * @return
	 * @throws GatewayException
	 */
	GatewayBase get(GatewayType gatewayType) throws GatewayException;

	/**
	 * 通过网关类型,交易类型获取网关
	 * 
	 * @param gatewayType
	 * @param gatewayTradeType
	 * @return
	 * @throws GatewayException
	 */
	GatewayBase get(GatewayType gatewayType, GatewayTradeType gatewayTradeType) throws GatewayException;

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
