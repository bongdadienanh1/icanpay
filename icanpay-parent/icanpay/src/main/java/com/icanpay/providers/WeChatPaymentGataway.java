package com.icanpay.providers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.icanpay.GatewayBase;
import com.icanpay.GatewayParameter;
import com.icanpay.enums.GatewayType;
import com.icanpay.interfaces.PaymentQRCode;
import com.icanpay.utils.Utility;

/**
 * 微信支付网关
 * 
 * @author milanyangbo
 *
 */
public class WeChatPaymentGataway extends GatewayBase implements PaymentQRCode {

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
		return null;
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
			if (!StringUtils.isBlank(val) && !key.equals("sign")) {
				signBuilder.append(String.format("%s=%s&", key, val));
			}
		});
		signBuilder.append(String.format("key=%s", getMerchant().getKey()));
		return Utility.getMD5(signBuilder.toString()).toUpperCase();
	}

	@Override
	protected boolean checkNotifyData() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String writeSucceedFlag() {
		// TODO Auto-generated method stub
		return null;
	}

}
