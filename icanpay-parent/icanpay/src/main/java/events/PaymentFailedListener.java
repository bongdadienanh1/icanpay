package events;


public interface PaymentFailedListener {

	void handleEvent(PaymentFailedEventArgs event);
}
