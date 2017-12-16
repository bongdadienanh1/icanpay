package com.icanpay.gateways;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.icanpay.Merchant;
import com.icanpay.enums.GatewayTradeType;
import com.icanpay.enums.GatewayType;
import com.icanpay.exceptions.GatewayException;

public class GatewaysImpl implements Gateways {

	private List<GatewayBase> _list;

	public int count;

	public GatewaysImpl() {
		_list = new ArrayList<GatewayBase>();
	}

	@Override
	public boolean add(GatewayBase gateway) throws GatewayException {
		// TODO Auto-generated method stub
		if (gateway != null) {
			if (!_list.stream().anyMatch(
					g -> g.getMerchant().getAppId()
							.equals(gateway.getMerchant().getAppId()))) {
				_list.add(gateway);

				return true;
			} else {
				throw new GatewayException("该商户数据已存在");
			}
		}

		return false;
	}

	@Override
	public GatewayBase get(GatewayType gatewayType) throws GatewayException {
		// TODO Auto-generated method stub
		GatewayBase gateway = _list.stream()
				.filter(a -> a.getGatewayType() == gatewayType).findFirst()
				.orElse(null);

		if (gateway == null) {
			throw new GatewayException("找不到指定网关");
		}

		return gateway;
	}

	@Override
	public GatewayBase get(GatewayType gatewayType,
			GatewayTradeType gatewayTradeType) throws GatewayException {
		// TODO Auto-generated method stub
		GatewayBase gateway = _list
				.stream()
				.filter(a -> a.getGatewayType() == gatewayType
						&& a.getGatewayTradeType() == gatewayTradeType)
				.findFirst().orElse(null);
		if (gateway == null) {
			gateway = get(gatewayType);
			if (gateway == null) {
				throw new GatewayException("找不到指定网关");
			}
		}
		gateway.setGatewayTradeType(gatewayTradeType);
		return gateway;
	}

	@Override
	public List<GatewayBase> getList() {
		// TODO Auto-generated method stub
		return _list;
	}

	@Override
	public List<Merchant> getMerchants() {
		// TODO Auto-generated method stub
		return _list.stream().map(m -> m.getMerchant())
				.collect(Collectors.toList());
	}

	public int getCount() {
		return _list.size();
	}

}
