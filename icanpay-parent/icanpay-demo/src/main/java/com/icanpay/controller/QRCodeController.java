package com.icanpay.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.unionpay.acp.sdk.SDKConfig;

@RestController
@RequestMapping("/qrcodepayment")
public class QRCodeController {

	@Autowired
	Gateways gateways;

	public QRCodeController(Gateways gateways) {
		this.gateways = gateways;
	}

	@GetMapping("/createorder")
	public void createOrder(Integer type) throws IOException, Exception {
		GatewayType gatewayType = GatewayType.Alipay;
		if (type == 0) {
			gatewayType = GatewayType.Alipay;
		}
		if (type == 1) {
			gatewayType = GatewayType.WeChatPay;
		}
		if (type == 2) {
			SDKConfig.getConfig().loadPropertiesFromSrc();
			gatewayType = GatewayType.UnionPay;
		}

		GatewayBase gateway = gateways
				.get(gatewayType, GatewayTradeType.QRCode);
		PaymentSetting paymentSetting = new PaymentSetting(gateway);
		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo(
				new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		paymentSetting.getOrder().setPaymentDate(new Date());
		paymentSetting.getOrder().setSubject("qrcodepay");
		paymentSetting.payment(null);
	}
}
