package com.icanpay.controller;

import com.icanpay.Order;
import com.icanpay.PaymentSetting;
import com.icanpay.enums.GatewayType;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.Gateways;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/querypayment")
public class QueryPaymentController {

	@Autowired
	Gateways gateways;

	@GetMapping("/createquery")
	public void createQuery(int type) {
		// 查询时需要设置订单的Id与金额，在查询结果中将会核对订单的Id与金额，如果不相符会返回查询失败。
		GatewayBase gateway = gateways.get(GatewayType.getGatewayType(type));
		boolean query = PaymentSetting.buid(gateway).setOrder(
				Order.OrderBuilder.builder()
						.orderAmount(0.01)
						.orderNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()))
						.build())
				.queryNow();

		if (query) {
			// 订单已支付
		}
	}

}
