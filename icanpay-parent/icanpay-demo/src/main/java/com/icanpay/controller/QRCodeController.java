package com.icanpay.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icanpay.PaymentSetting;
import com.icanpay.enums.GatewayTradeType;
import com.icanpay.enums.GatewayType;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.Gateways;

@RestController
@RequestMapping("/qrcodepayment")
public class QRCodeController {

	@Autowired
	Gateways gateways;

	@GetMapping("/createorder")
	public void createOrder(int type) {
		GatewayType gatewayType = GatewayType.getGatewayType(type);
		GatewayBase gateway = gateways.get(gatewayType, GatewayTradeType.QRCode);
		PaymentSetting paymentSetting = new PaymentSetting(gateway);
		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		paymentSetting.getOrder().setPaymentDate(new Date());
		paymentSetting.getOrder().setSubject("qrcodepay");
		paymentSetting.payment(null);
	}
}
