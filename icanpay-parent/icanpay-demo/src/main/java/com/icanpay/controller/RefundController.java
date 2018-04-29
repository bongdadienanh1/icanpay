package com.icanpay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icanpay.PaymentSetting;
import com.icanpay.Refund;
import com.icanpay.enums.GatewayType;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.Gateways;

@RestController
@RequestMapping("/refund")
public class RefundController {

	@Autowired
	Gateways gateways;

	@GetMapping("/createrefund")
	public void createRefund(int type) {
		GatewayType gatewayType = GatewayType.getGatewayType(type);
		GatewayBase gateway = gateways.get(gatewayType);
		PaymentSetting paymentSetting = new PaymentSetting(gateway);

		Refund refund = new Refund();
		refund.setOutRefoundNo("0000000000");

		paymentSetting.buildRefund(refund);
		paymentSetting.buildRefundQuery(refund);
	}

}
