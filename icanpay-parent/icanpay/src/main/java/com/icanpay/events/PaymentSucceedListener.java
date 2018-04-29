package com.icanpay.events;

import java.util.EventListener;

@FunctionalInterface
public interface PaymentSucceedListener extends EventListener {

	void handleEvent(PaymentSucceedEventArgs event);
}
