package com.icanpay.events;



public interface PaymentFailedListener {

	void handleEvent(PaymentFailedEventArgs event);
}
