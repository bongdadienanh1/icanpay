package com.icanpay.providers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.icanpay.GatewayBase;
import com.icanpay.GatewayParameter;
import com.icanpay.enums.GatewayType;
import com.icanpay.enums.PaymentNotifyMethod;
import com.icanpay.enums.ProductSet;
import com.icanpay.interfaces.AppParams;
import com.icanpay.interfaces.PaymentForm;
import com.icanpay.interfaces.QueryNow;
import com.icanpay.interfaces.WapPaymentUrl;
import com.icanpay.utils.Utility;

public class AlipayGateway extends GatewayBase implements PaymentForm,
		WapPaymentUrl, AppParams, QueryNow {

	final String payGatewayUrl = "https://mapi.alipay.com/gateway.do";
	final String openapiGatewayUrl = "https://openapi.alipay.com/gateway.do";
	final String emailRegexString = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	String pageEncoding = "";

	/**
	 * 初始化支付宝网关
	 */
	public AlipayGateway() {
		pageEncoding = getCharset();
	}

	/**
	 * 初始化支付宝网关
	 * 
	 * @param gatewayParameterData
	 *            网关通知的数据集合
	 */
	public AlipayGateway(List<GatewayParameter> gatewayParameterData) {
		super(gatewayParameterData);
		pageEncoding = getCharset();
	}

	public GatewayType getGatewayType() {
		return GatewayType.Alipay;
	}

	@Override
	public String buildPaymentForm() throws Exception {
		// TODO Auto-generated method stub
		AlipayClient alipayClient = new DefaultAlipayClient(openapiGatewayUrl,
				getMerchant().getAppId(), getMerchant().getPrivateKeyPem(),
				"json", getCharset(), getMerchant().getPublicKeyPem(), "RSA"); // 获得初始化的AlipayClient

		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();// 创建API对应的request
		alipayRequest.setReturnUrl(getMerchant().getReturnUrl().toString());
		alipayRequest.setNotifyUrl(getMerchant().getNotifyUrl().toString());// 在公共参数中设置回跳和通知地址

		AlipayTradePagePayModel model = new AlipayTradePagePayModel();
		model.setSubject(getOrder().getSubject());
		model.setOutTradeNo(getOrder().getOrderNo());
		model.setTimeoutExpress("30m");
		model.setTotalAmount(String.valueOf(getOrder().getOrderAmount()));
		model.setProductCode("FAST_INSTANT_TRADE_PAY");
		alipayRequest.setBizModel(model);

		return alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
	}

	/*
	 * @Override public String buildPaymentUrl() throws Exception { // TODO
	 * Auto-generated method stub initOrderParameter("MD5");
	 * validatePaymentOrderParameter(); return String.format("%s?%s",
	 * payGatewayUrl, getPaymentQueryString()); }
	 */

	@Override
	public String buildWapPaymentUrl(Map<String, String> map)
			throws AlipayApiException {
		// TODO Auto-generated method stub
		AlipayClient alipayClient = new DefaultAlipayClient(openapiGatewayUrl,
				getMerchant().getAppId(), getMerchant().getPrivateKeyPem(),
				"json", getCharset(), getMerchant().getPublicKeyPem(), "RSA"); // 获得初始化的AlipayClient

		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		alipayRequest.setReturnUrl(getMerchant().getReturnUrl().toString());
		alipayRequest.setNotifyUrl(getMerchant().getNotifyUrl().toString());// 在公共参数中设置回跳和通知地址

		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setSubject(getOrder().getSubject());
		model.setOutTradeNo(getOrder().getOrderNo());
		model.setTimeoutExpress("30m");
		model.setTotalAmount(String.valueOf(getOrder().getOrderAmount()));
		model.setProductCode("QUICK_WAP_PAY");
		alipayRequest.setBizModel(model);

		return alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
	}

	@Override
	public Map<String, String> buildPayParams() throws AlipayApiException {
		// TODO Auto-generated method stub
		AlipayClient alipayClient = new DefaultAlipayClient(openapiGatewayUrl,
				getMerchant().getAppId(), getMerchant().getPrivateKeyPem(),
				"json", getCharset(), getMerchant().getPublicKeyPem(), "RSA"); // 获得初始化的AlipayClient

		AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();// 创建API对应的request
		alipayRequest.setReturnUrl(getMerchant().getReturnUrl().toString());
		alipayRequest.setNotifyUrl(getMerchant().getNotifyUrl().toString());// 在公共参数中设置回跳和通知地址

		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setSubject(getOrder().getSubject());
		model.setOutTradeNo(getOrder().getOrderNo());
		model.setTimeoutExpress("30m");
		model.setTotalAmount(String.valueOf(getOrder().getOrderAmount()));
		model.setProductCode("QUICK_MSECURITY_PAY");
		alipayRequest.setBizModel(model);

		Map<String, String> resParam = new HashMap<String, String>();
		resParam.put("body", alipayClient.pageExecute(alipayRequest).getBody());
		return resParam;
	}

	@Override
	public boolean queryNow(ProductSet productSet) throws AlipayApiException {
		// TODO Auto-generated method stub
		AlipayClient alipayClient = new DefaultAlipayClient(openapiGatewayUrl,
				getMerchant().getAppId(), getMerchant().getPrivateKeyPem(),
				"json", getCharset(), getMerchant().getPrivateKeyPem(), "RSA"); // 获得初始化的AlipayClient

		AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();// 创建API对应的request类

		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
		model.setOutTradeNo(getOrder().getOrderNo());
		alipayRequest.setBizModel(model);

		AlipayTradeQueryResponse response = alipayClient.execute(alipayRequest);// 通过alipayClient调用API，获得对应的response类

		if (((response.getTradeStatus().equalsIgnoreCase("TRADE_FINISHED") || response
				.getTradeStatus().equalsIgnoreCase("TRADE_SUCCESS")))) {
			double orderAmount = Double.parseDouble(response.getTotalAmount());
			if (getOrder().getOrderAmount() == orderAmount
					&& getOrder().getOrderNo().equals(response.getOutTradeNo())) {
				return true;
			}
			return false;
		}
		return false;

	}

	@Override
	protected boolean checkNotifyData() throws AlipayApiException {
		// TODO Auto-generated method stub
		if (validateAlipayNotifyRSASign()) {
			return validateTrade();
		}
		return false;
	}

	@Override
	public void writeSucceedFlag() throws IOException {
		// TODO Auto-generated method stub
		if (getPaymentNotifyMethod() == PaymentNotifyMethod.ServerNotify) {
			Utility.getHttpServletResponse().getWriter().write("success");
		}
	}

	@SuppressWarnings("unused")
	private void initOrderParameter(String sign_type) throws Exception {
		setGatewayParameterValue("seller_email", getMerchant().getEmail());
		setGatewayParameterValue("service", "create_direct_pay_by_user");
		setGatewayParameterValue("partner", getMerchant().getPartner());
		setGatewayParameterValue("notify_url", getMerchant().getNotifyUrl()
				.toString());
		setGatewayParameterValue("return_url", getMerchant().getReturnUrl()
				.toString());
		setGatewayParameterValue("sign_type", sign_type);
		setGatewayParameterValue("subject", getOrder().getSubject());
		setGatewayParameterValue("out_trade_no", getOrder().getOrderNo());
		setGatewayParameterValue("total_fee", getOrder().getOrderAmount());
		setGatewayParameterValue("payment_type", "1");
		setGatewayParameterValue("_input_charset", getCharset());
		setGatewayParameterValue("sign", getOrderSign()); // 签名需要在最后设置，以免缺少参数。
	}

	/**
	 * 获得订单的签名
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getOrderSign() throws Exception {
		// TODO Auto-generated method stub
		return Utility.getMD5(getSignParameter() + getMerchant().getKey(),
				pageEncoding).toLowerCase();
	}

	/**
	 * 获得用于签名的参数字符串
	 * 
	 * @return
	 */
	private String getSignParameter() {
		// TODO Auto-generated method stub
		StringBuilder signBuilder = new StringBuilder();
		getSortedGatewayParameter().forEach((key, val) -> {
			if (!key.equals("sign") && !key.equals("sign")) {
				signBuilder.append(String.format("%s=%s&", key, val));
			}
		});
		String text = signBuilder.toString();
		return text.substring(0, text.length() - 1);
	}

	@SuppressWarnings("unused")
	private String getPaymentQueryString() {
		// TODO Auto-generated method stub
		StringBuilder signBuilder = new StringBuilder();
		getSortedGatewayParameter().forEach((key, val) -> {
			if (!key.equals("sign") && !key.equals("sign")) {
				signBuilder.append(String.format("%s=%s&", key, val));
			}
		});
		String text = signBuilder.toString();
		return text.substring(0, text.length() - 1);
	}

	/**
	 * 验证支付订单的参数设置
	 */
	@SuppressWarnings("unused")
	private void validatePaymentOrderParameter() {
		if (Utility.isBlankOrEmpty(getGatewayParameterValue("seller_email"))) {
			throw new IllegalArgumentException(
					"订单缺少seller_email参数，seller_email是卖家支付宝账号的邮箱。");
		}
		// TODO Auto-generated method stub
		if (!isEmail(getGatewayParameterValue("seller_email"))) {
			throw new IllegalArgumentException("Email格式不正确-seller_email");
		}
	}

	/**
	 * 是否是正确格式的Email地址
	 * 
	 * @param emailAddress
	 * @return
	 */
	public boolean isEmail(String emailAddress) {
		if (Utility.isBlankOrEmpty(emailAddress)) {
			return false;
		}

		return emailAddress.matches(emailRegexString);
	}

	/**
	 * 验证支付宝通知的签名
	 * 
	 * @return
	 * @throws AlipayApiException
	 */
	private boolean validateAlipayNotifyRSASign() throws AlipayApiException {
		// TODO Auto-generated method stub
		boolean checkSign = AlipaySignature.rsaCheckV1(
				getSortedGatewayParameter(), getMerchant().getPublicKeyPem(),
				getCharset());
		if (checkSign) {
			return true;
		}
		return false;
	}

	private boolean validateTrade() {
		// TODO Auto-generated method stub
		// 支付状态是否为成功。TRADE_FINISHED（普通即时到账的交易成功状态，TRADE_SUCCESS（开通了高级即时到账或机票分销产品后的交易成功状态）
		if (getGatewayParameterValue("trade_status").equalsIgnoreCase(
				"TRADE_FINISHED")
				|| getGatewayParameterValue("trade_status").equalsIgnoreCase(
						"TRADE_SUCCESS")) {
			String orderAmount = getGatewayParameterValue("total_amount");
			orderAmount = Utility.isBlankOrEmpty(orderAmount) ? getGatewayParameterValue("total_fee")
					: orderAmount;
			getOrder().setOrderAmount(Double.parseDouble(orderAmount));
			getOrder().setOrderNo(getGatewayParameterValue("out_trade_no"));
			getOrder().setTradeNo(getGatewayParameterValue("trade_no"));
			return true;
		}
		return false;
	}

}
