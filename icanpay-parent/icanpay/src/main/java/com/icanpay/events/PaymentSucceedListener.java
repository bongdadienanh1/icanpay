package com.icanpay.events;

import java.util.EventListener;

public interface PaymentSucceedListener extends EventListener {

	void handleEvent(PaymentSucceedEventArgs event);
}
