package com.icanpay.controller;

import com.icanpay.Order;
import com.icanpay.PaymentSetting;
import com.icanpay.enums.GatewayTradeType;
import com.icanpay.enums.GatewayType;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.Gateways;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/apppayment")
public class AppPaymentController {

	@Autowired
	Gateways gateways;

	@GetMapping("/createorder")
	public Map<String, String> createOrder(int type) {
		GatewayBase gateway = gateways.get(GatewayType.getGatewayType(type), GatewayTradeType.APP);
		return PaymentSetting.buid(gateway).setOrder(
				Order.newBuilder()
						.orderAmount(0.01)
						.orderNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()))
						.paymentDate(new Date())
						.subject("apppay")
						.build())
				.payment();
	}
}
