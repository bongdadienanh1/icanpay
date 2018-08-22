# icanpay
统一支付网关。支持支付宝，微信，银联支付渠道通过Web，App，Wap，QRCode方式支付。简化订单的创建、查询跟接收网关返回的支付通知等功能

WebPayment（网站支付）
   @GetMapping("/createorder")
	public void createOrder(int type) {
		GatewayType gatewayType = GatewayType.getGatewayType(type);
		GatewayBase gateway = gateways.get(gatewayType, GatewayTradeType.Web);
		PaymentSetting paymentSetting = new PaymentSetting(gateway);
		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		paymentSetting.getOrder().setPaymentDate(new Date());
		paymentSetting.getOrder().setSubject("webpay");
		paymentSetting.payment(null);
	}
	
WapPayment（手机网站支付）
   @GetMapping("/createorder")
	public void createOrder(int type) {
		GatewayType gatewayType = GatewayType.getGatewayType(type);
		GatewayBase gateway = gateways.get(gatewayType, GatewayTradeType.Wap);
		PaymentSetting paymentSetting = new PaymentSetting(gateway);
		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		paymentSetting.getOrder().setPaymentDate(new Date());
		paymentSetting.getOrder().setSubject("webpay");
		paymentSetting.payment(null);
	}
	
QRCodePayment（二维码支付）
	@GetMapping("/createorder")
	public void createOrder(int type) {
		GatewayType gatewayType = GatewayType.getGatewayType(type);
		GatewayBase gateway = gateways.get(gatewayType, GatewayTradeType.QRCode);
		PaymentSetting paymentSetting = new PaymentSetting(gateway);
		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		paymentSetting.getOrder().setPaymentDate(new Date());
		paymentSetting.getOrder().setSubject("qrcodepay");
		paymentSetting.payment(null);
	}
	
AppPayment（手机APP支付）
	@GetMapping("/createorder")
	public Map<String, String> createOrder(int type) {
		GatewayType gatewayType = GatewayType.getGatewayType(type);
		GatewayBase gateway = gateways.get(gatewayType, GatewayTradeType.APP);
		PaymentSetting paymentSetting = new PaymentSetting(gateway);
		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		paymentSetting.getOrder().setPaymentDate(new Date());
		paymentSetting.getOrder().setSubject("apppay");
		return paymentSetting.payment(null);
	}
	
QueryPayment（查询订单）
   @GetMapping("/createquery")
	public void createQuery(int type) {
		GatewayType gatewayType = GatewayType.getGatewayType(type);
		GatewayBase gateway = gateways.get(gatewayType);
		PaymentSetting paymentSetting = new PaymentSetting(gateway);

		// 查询时需要设置订单的Id与金额，在查询结果中将会核对订单的Id与金额，如果不相符会返回查询失败。
		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo("yourorderno");

		if (paymentSetting.queryNow()) {
			// 订单已支付
		}
	}

Notify（异步通知）
	PaymentNotify paymentNotify;

	@Autowired
	public NotifyController(PaymentNotify paymentNotify) {
		this.paymentNotify = paymentNotify;
		paymentNotify.setPaymentSucceed(event -> {

			// 支付成功时时的处理代码
			if (event.getPaymentNotifyMethod() == PaymentNotifyMethod.AutoReturn) {
				// 当前是用户的浏览器自动返回时显示充值成功页面
			} else {
				// 支付结果的发送方式，以服务端接收为准
			}

		});

		paymentNotify.setPaymentFailed(event -> {
			// 支付失败时的处理代码
		});

		paymentNotify.setUnknownGateway(event -> {
			// 无法识别支付网关时的处理代码
		});

	}

	@GetMapping("/servernotify")
	public void ServerNotify() {
		// 接收并处理支付通知
		paymentNotify.received(PaymentNotifyMethod.ServerNotify);
	}

	@GetMapping("/autoreturn")
	public void AutoReturn() {
		// 接收并处理支付通知
		paymentNotify.received(PaymentNotifyMethod.AutoReturn);
	}

## Wiki
 * [如何使用](https://github.com/milanyangbo/icanpay-paysdk/wiki/Getting-started)

 * [实现基类支持更多支付网关](https://github.com/milanyangbo/icanpay-paysdk/wiki/Implement-a-new-gateway)

