package events;

public interface PaymentSucceedListener {

	void handleEvent(PaymentSucceedEventArgs event);
}
