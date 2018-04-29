package com.icanpay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icanpay.PaymentSetting;
import com.icanpay.enums.GatewayType;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.Gateways;

@RestController
@RequestMapping("/querypayment")
public class QueryPaymentController {

	@Autowired
	Gateways gateways;

	@GetMapping("/createquery")
	public void createQuery(int type) {
		GatewayType gatewayType = GatewayType.getGatewayType(type);
		GatewayBase gateway = gateways.get(gatewayType);
		PaymentSetting paymentSetting = new PaymentSetting(gateway);

		// 查询时需要设置订单的Id与金额，在查询结果中将会核对订单的Id与金额，如果不相符会返回查询失败。
		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo("yourorderno");

		if (paymentSetting.queryNow()) {
			// 订单已支付
		}
	}

}
