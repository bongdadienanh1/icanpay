package com.icanpay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icanpay.enums.PaymentNotifyMethod;
import com.icanpay.events.PaymentNotify;
import com.icanpay.gateways.Gateways;

@RestController
@RequestMapping("/notify")
public class NotifyController {

	PaymentNotify notify;

	@Autowired
	Gateways gateways;

	public NotifyController(Gateways gateways) {
		this.gateways = gateways;

		// 添加到商户数据集合
		notify = new PaymentNotify(gateways.getMerchants());

		notify.setPaymentFailed(event -> {

			// 支付成功时时的处理代码
			if (event.getPaymentNotifyMethod() == PaymentNotifyMethod.AutoReturn) {
				// 当前是用户的浏览器自动返回时显示充值成功页面
			} else {
				// 支付结果的发送方式，以服务端接收为准
			}

		});

		notify.setPaymentFailed(event -> {

		});

		notify.setUnknownGateway(event -> {

		});

	}

	@GetMapping("/servernotify")
	public void ServerNotify() throws Exception {
		// 接收并处理支付通知
		notify.received(PaymentNotifyMethod.ServerNotify);
	}

	@GetMapping("/autoreturn")
	public void AutoReturn() throws Exception {
		// 接收并处理支付通知
		notify.received(PaymentNotifyMethod.AutoReturn);
	}
}
