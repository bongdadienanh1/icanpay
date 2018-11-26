package com.icanpay.providers;

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
import com.icanpay.enums.GatewayType;
import com.icanpay.enums.PaymentNotifyMethod;
import com.icanpay.exceptions.GatewayException;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.GatewayParameter;
import com.icanpay.interfaces.PaymentApp;
import com.icanpay.interfaces.PaymentForm;
import com.icanpay.interfaces.QueryNow;
import com.icanpay.interfaces.WapPaymentUrl;
import com.icanpay.utils.Utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlipayGateway extends GatewayBase implements PaymentForm, WapPaymentUrl, PaymentApp, QueryNow {

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

	@Override
	public GatewayType getGatewayType() {
		return GatewayType.Alipay;
	}

	@Override
	public String buildPaymentForm() {
		// TODO Auto-generated method stub
		AlipayClient alipayClient = getAopClient(); // 获得初始化的AlipayClient

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

		try {
			// 调用SDK生成表单
			return alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			throw new GatewayException(e.getMessage(), e);
		}
	}

	@Override
	public String buildWapPaymentUrl(Map<String, String> map) {
		// TODO Auto-generated method stub
		AlipayClient alipayClient = getAopClient(); // 获得初始化的AlipayClient

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

		try {
			// 调用SDK生成表单
			return alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			throw new GatewayException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, String> buildPayParams() {
		// TODO Auto-generated method stub
		AlipayClient alipayClient = getAopClient(); // 获得初始化的AlipayClient

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
		try {
			resParam.put("body", alipayClient.pageExecute(alipayRequest).getBody());
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			throw new GatewayException(e.getMessage(), e);
		}
		return resParam;
	}

	@Override
	public boolean queryNow() {
		// TODO Auto-generated method stub
		AlipayClient alipayClient = getAopClient(); // 获得初始化的AlipayClient

		AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();// 创建API对应的request类

		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
		model.setOutTradeNo(getOrder().getOrderNo());
		alipayRequest.setBizModel(model);

		AlipayTradeQueryResponse response = null;
		try {
			// 通过alipayClient调用API，获得对应的response类
			response = alipayClient.execute(alipayRequest);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			throw new GatewayException(e.getMessage(), e);
		}

		if (((response.getTradeStatus().equalsIgnoreCase("TRADE_FINISHED") || response.getTradeStatus().equalsIgnoreCase("TRADE_SUCCESS")))) {
			double orderAmount = Double.parseDouble(response.getTotalAmount());
			if (getOrder().getOrderAmount() == orderAmount && getOrder().getOrderNo().equals(response.getOutTradeNo())) {
				return true;
			}
			return false;
		}
		return false;

	}


	@Override
	protected boolean checkNotifyData() {
		// TODO Auto-generated method stub
		if (validateAlipayNotifyRSASign()) {
			return validateTrade();
		}
		return false;
	}

	@Override
	public void writeSucceedFlag() {
		// TODO Auto-generated method stub
		if (getPaymentNotifyMethod() == PaymentNotifyMethod.ServerNotify) {
			try {
				Utility.getHttpServletResponse().getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new GatewayException(e.getMessage(), e);
			}
		}
	}

	public AlipayClient getAopClient() {
		return new DefaultAlipayClient(openapiGatewayUrl, getMerchant().getAppId(), getMerchant().getPrivateKeyPem(), "json", getCharset(),
				getMerchant().getPrivateKeyPem(), "RSA");
	}

	/**
	 * 验证支付宝通知的签名
	 *
	 * @return
	 * @throws AlipayApiException
	 */
	private boolean validateAlipayNotifyRSASign() {
		// TODO Auto-generated method stub
		boolean checkSign = false;
		try {
			checkSign = AlipaySignature.rsaCheckV1(getSortedGatewayParameter(), getMerchant().getPublicKeyPem(), getCharset());
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			throw new GatewayException(e.getMessage(), e);
		}
		if (checkSign) {
			return true;
		}
		return false;
	}

	/**
	 * 验证支付状态
	 *
	 * @return
	 */
	private boolean validateTrade() {
		// TODO Auto-generated method stub
		// 支付状态是否为成功。TRADE_FINISHED（普通即时到账的交易成功状态，TRADE_SUCCESS（开通了高级即时到账或机票分销产品后的交易成功状态）
		if (getGatewayParameterValue("trade_status").equalsIgnoreCase("TRADE_FINISHED") || getGatewayParameterValue("trade_status")
				.equalsIgnoreCase("TRADE_SUCCESS")) {
			String orderAmount = getGatewayParameterValue("total_amount");
			orderAmount = Utility.isBlankOrEmpty(orderAmount) ? getGatewayParameterValue("total_fee") : orderAmount;
			getOrder().setOrderAmount(Double.parseDouble(orderAmount));
			getOrder().setOrderNo(getGatewayParameterValue("out_trade_no"));
			getOrder().setTradeNo(getGatewayParameterValue("trade_no"));
			return true;
		}
		return false;
	}

}
