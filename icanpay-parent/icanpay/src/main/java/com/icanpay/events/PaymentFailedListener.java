package com.icanpay.events;

import java.util.EventListener;

public interface PaymentFailedListener extends EventListener {

	void handleEvent(PaymentFailedEventArgs event);
}
