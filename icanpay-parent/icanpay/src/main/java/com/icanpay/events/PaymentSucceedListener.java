package com.icanpay.events;


public interface PaymentSucceedListener {

	void handleEvent(PaymentSucceedEventArgs event);
}
