package com.icanpay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icanpay.enums.PaymentNotifyMethod;
import com.icanpay.events.PaymentNotify;

@RestController
@RequestMapping("/notify")
public class NotifyController {

	@Autowired
	PaymentNotify notify;

	public NotifyController() {

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
	public void ServerNotify() {
		// 接收并处理支付通知
		notify.received(PaymentNotifyMethod.ServerNotify);
	}

	@GetMapping("/autoreturn")
	public void AutoReturn() {
		// 接收并处理支付通知
		notify.received(PaymentNotifyMethod.AutoReturn);
	}
}
