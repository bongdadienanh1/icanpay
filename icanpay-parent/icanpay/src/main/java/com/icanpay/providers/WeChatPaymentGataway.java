package com.icanpay.providers;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.icanpay.GatewayBase;
import com.icanpay.GatewayParameter;
import com.icanpay.enums.GatewayType;
import com.icanpay.enums.ProductSet;
import com.icanpay.interfaces.AppParams;
import com.icanpay.interfaces.PaymentQRCode;
import com.icanpay.interfaces.QueryNow;
import com.icanpay.interfaces.WapPaymentUrl;
import com.icanpay.utils.HttpClientUtil;
import com.icanpay.utils.Utility;

/**
 * 微信支付网关
 * 
 * @author milanyangbo
 *
 */
public class WeChatPaymentGataway extends GatewayBase implements PaymentQRCode,
		WapPaymentUrl, AppParams, QueryNow {

	final String payGatewayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	final String queryGatewayUrl = "https://api.mch.weixin.qq.com/pay/orderquery";

	/**
	 * 初始化微信支付网关
	 */
	public WeChatPaymentGataway() {
	}

	/**
	 * 初始化微信支付网关
	 * 
	 * @param gatewayParameterData
	 *            网关通知的数据集合
	 */
	public WeChatPaymentGataway(List<GatewayParameter> gatewayParameterData) {
		super(gatewayParameterData);
	}

	public GatewayType getGatewayType() {
		return GatewayType.WeChatPayment;
	}

	@Override
	public String getPaymentQRCodeContent() throws Exception {
		// TODO Auto-generated method stub
		initPaymentOrderParameter("NATIVE", "127.0.0.1");
		String xmlString = convertGatewayParameterDataToXml();
		String resultXml = HttpClientUtil.doPost(xmlString, payGatewayUrl,
				"text/xml");
		return getWeixinPaymentUrl(resultXml);
	}

	/**
	 * https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=15_4
	 */
	@Override
	public String buildWapPaymentUrl(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		String redirect_url = map.getOrDefault("redirect_url", "");
		String spbill_create_ip = map.getOrDefault("spbill_create_ip", "");
		initPaymentOrderParameter("MWEB", spbill_create_ip);
		String xmlString = convertGatewayParameterDataToXml();
		String resultXml = HttpClientUtil.doPost(xmlString, payGatewayUrl,
				"text/xml");
		String url = getWeixinPaymentUrl(resultXml);
		redirect_url = Utility.isBlankOrEmpty(redirect_url) ? getMerchant()
				.getReturnUrl().toString() : redirect_url;
		redirect_url = URLEncoder.encode(redirect_url, "UTF-8");
		return String.format("%s&redirect_url=%s", url, redirect_url);
	}

	@Override
	public Map<String, String> buildPayParams() throws Exception {
		// TODO Auto-generated method stub
		initPaymentOrderParameter("APP", "127.0.0.1");
		String xmlString = convertGatewayParameterDataToXml();
		String resultString = HttpClientUtil.doPost(xmlString, payGatewayUrl,
				"text/xml");
		getWeixinPaymentUrl(resultString);

		String prepayid = getGatewayParameterValue("prepay_id");
		clearGatewayParameterData();

		setGatewayParameterValue("appid", getMerchant().getAppId());
		setGatewayParameterValue("partnerid", getMerchant().getPartner());
		setGatewayParameterValue("prepayid", prepayid);
		setGatewayParameterValue("package", "Sign=WXPay");
		setGatewayParameterValue("noncestr", Utility.generateUUID());
		setGatewayParameterValue("timestamp", Utility.getCurrentTimestampMs());
		setGatewayParameterValue("sign", getSign());

		Map<String, String> resParam = new HashMap<String, String>();
		resParam.put("prepayid", prepayid);
		resParam.put("noncestr", getGatewayParameterValue("noncestr"));
		resParam.put("sign", getGatewayParameterValue("sign"));
		resParam.put("timestamp", getGatewayParameterValue("timestamp"));
		resParam.put("partnerid", getGatewayParameterValue("partnerid"));
		return resParam;
	}

	@Override
	public boolean queryNow(ProductSet productSet, HttpServletRequest req)
			throws Exception {
		// TODO Auto-generated method stub
		initQueryOrderParameter();
		String xmlString = convertGatewayParameterDataToXml();
		String resultXml = HttpClientUtil.doPost(xmlString, payGatewayUrl,
				"text/xml");
		return checkQueryResult(resultXml);
	}

	private boolean checkQueryResult(String resultXml) throws Exception {
		// TODO Auto-generated method stub
		// 需要先清除之前查询订单的参数，否则会对接收到的参数造成干扰。
		clearGatewayParameterData();
		readResultXml(resultXml);
		if (isSuccessResult()) {
			if (!Utility.isBlankOrEmpty(getGatewayParameterValue("total_fee"))) {
				if (getOrder().getOrderNo().equals(
						getGatewayParameterValue("out_trade_no"))
						&& getOrder().getOrderAmount() == Integer
								.parseInt(getGatewayParameterValue("total_fee")) / 100.0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean checkNotifyData(HttpServletRequest req) throws Exception {
		// TODO Auto-generated method stub
		readNotifyOrderParameter();
		if (isSuccessResult()) {
			return true;
		}
		return false;
	}

	@Override
	public String writeSucceedFlag() throws Exception {
		// TODO Auto-generated method stub
		clearGatewayParameterData();
		initProcessSuccessParameter();
		return convertGatewayParameterDataToXml();
	}

	/**
	 * 初始化表示已成功接收到支付通知的数据
	 */
	private void initProcessSuccessParameter() {
		// TODO Auto-generated method stub
		setGatewayParameterValue("return_code", "SUCCESS");
	}

	private void readNotifyOrderParameter() {
		// TODO Auto-generated method stub
		getOrder().setOrderNo(getGatewayParameterValue("out_trade_no"));
		getOrder().setOrderAmount(
				Integer.parseInt(getGatewayParameterValue("total_fee")) * 0.01);
		getOrder().setTradeNo(getGatewayParameterValue("transaction_id"));
	}

	/**
	 * 初始化支付订单的参数
	 * 
	 * @throws Exception
	 */
	private void initQueryOrderParameter() throws Exception {
		// TODO Auto-generated method stub
		setGatewayParameterValue("appid", getMerchant().getAppId());
		setGatewayParameterValue("mch_id", getMerchant().getPartner());
		setGatewayParameterValue("out_trade_no", getOrder().getOrderNo());
		setGatewayParameterValue("nonce_str", Utility.generateUUID());
		setGatewayParameterValue("sign", getSign()); // 签名需要在最后设置，以免缺少参数。
	}

	/**
	 * 获得微信支付的URL
	 * 
	 * @param resultString
	 * @return 创建订单返回的数据
	 * @throws Exception
	 */
	private String getWeixinPaymentUrl(String resultXml) throws Exception {
		// TODO Auto-generated method stub
		// 需要先清除之前创建订单的参数，否则会对接收到的参数造成干扰。
		clearGatewayParameterData();
		readResultXml(resultXml);
		if (isSuccessResult()) {
			return Utility.isBlankOrEmpty(getGatewayParameterValue("code_url")) ? getGatewayParameterValue("mweb_url")
					: getGatewayParameterValue("code_url");
		}

		return null;
	}

	/**
	 * 是否是成功的结果
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean isSuccessResult() throws Exception {
		// TODO Auto-generated method stub
		if (validateResult() && validateSign()) {
			return true;
		}

		return false;
	}

	/**
	 * 验证返回的结果
	 * 
	 * @return
	 */
	private boolean validateResult() {
		// TODO Auto-generated method stub
		if (getGatewayParameterValue("return_code").equals("SUCCESS")
				&& getGatewayParameterValue("result_code").equals("SUCCESS")) {
			return true;
		}

		return false;
	}

	/**
	 * 验证签名
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean validateSign() throws Exception {
		// TODO Auto-generated method stub
		if (getGatewayParameterValue("sign").equals(getSign())) {
			return true;
		}

		return false;
	}

	/**
	 * 读取结果的XML
	 * 
	 * @param resultXml
	 * @throws Exception
	 */
	private void readResultXml(String resultXml) throws Exception {
		// TODO Auto-generated method stub
		Utility.xmlToMap(resultXml).forEach((key, val) -> {
			setGatewayParameterValue(key, val);
		});
	}

	/**
	 * 清除网关的数据
	 */
	private void clearGatewayParameterData() {
		// TODO Auto-generated method stub
		getGatewayParameterData().clear();
	}

	/**
	 * 将网关数据转换成XML
	 * 
	 * @return
	 * @throws Exception
	 */
	private String convertGatewayParameterDataToXml() throws Exception {
		// TODO Auto-generated method stub
		return Utility.mapToXml(getSortedGatewayParameter());
	}

	/**
	 * 初始化支付订单的参数
	 * 
	 * @param trade_type
	 * @param spbill_create_ip
	 * @throws Exception
	 */
	private void initPaymentOrderParameter(String trade_type,
			String spbill_create_ip) throws Exception {

		setGatewayParameterValue("appid", getMerchant().getAppId());
		setGatewayParameterValue("mch_id", getMerchant().getPartner());
		setGatewayParameterValue("nonce_str", Utility.generateUUID());
		setGatewayParameterValue("body", getOrder().getSubject());
		setGatewayParameterValue("out_trade_no", getOrder().getOrderNo());
		setGatewayParameterValue("total_fee",
				(getOrder().getOrderAmount() * 100));
		setGatewayParameterValue("spbill_create_ip", spbill_create_ip);
		setGatewayParameterValue("notify_url", getMerchant().getNotifyUrl()
				.toString());
		setGatewayParameterValue("trade_type", trade_type);
		setGatewayParameterValue("sign", getSign()); // 签名需要在最后设置，以免缺少参数。
	}

	private String getSign() throws Exception {
		// TODO Auto-generated method stub
		StringBuilder signBuilder = new StringBuilder();
		getSortedGatewayParameter().forEach((key, val) -> {
			if (!Utility.isBlankOrEmpty(val) && !key.equals("sign")) {
				signBuilder.append(String.format("%s=%s&", key, val));
			}
		});
		signBuilder.append(String.format("key=%s", getMerchant().getKey()));
		return Utility.getMD5(signBuilder.toString()).toUpperCase();
	}

}
