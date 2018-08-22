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
import java.util.HashMap;

@RestController
@RequestMapping("/wappayment")
public class WapPaymentController {

	@Autowired
	Gateways gateways;

	@GetMapping("/createorder")
	public void createOrder(int type) {
		GatewayBase gateway = gateways.get(GatewayType.getGatewayType(type), GatewayTradeType.Wap);
		PaymentSetting.buid(gateway).setOrder(
				Order.OrderBuilder.builder()
						.orderAmount(0.01)
						.orderNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()))
						.paymentDate(new Date())
						.subject("wappay")
						.build())
				.payment(new HashMap<String, String>());
	}
}
