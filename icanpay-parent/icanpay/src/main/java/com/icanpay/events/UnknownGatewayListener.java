package com.icanpay.events;

import java.util.EventListener;

public interface UnknownGatewayListener extends EventListener {

	public void handleEvent(UnknownGatewayEventArgs event);
}
