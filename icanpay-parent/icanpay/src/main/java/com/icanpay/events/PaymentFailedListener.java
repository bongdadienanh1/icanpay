package com.icanpay.events;

import java.util.EventListener;

@FunctionalInterface
public interface PaymentFailedListener extends EventListener {

	void handleEvent(PaymentFailedEventArgs event);
}
