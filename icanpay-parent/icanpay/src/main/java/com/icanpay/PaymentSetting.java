package com.icanpay;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.NotImplementedException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.icanpay.enums.GatewayType;
import com.icanpay.enums.ProductSet;
import com.icanpay.interfaces.AppParams;
import com.icanpay.interfaces.PaymentForm;
import com.icanpay.interfaces.PaymentQRCode;
import com.icanpay.interfaces.PaymentUrl;
import com.icanpay.interfaces.QueryForm;
import com.icanpay.interfaces.QueryNow;
import com.icanpay.interfaces.QueryUrl;
import com.icanpay.interfaces.RefundReq;
import com.icanpay.interfaces.WapPaymentForm;
import com.icanpay.interfaces.WapPaymentUrl;
import com.icanpay.providers.AlipayGateway;
import com.icanpay.providers.NullGateway;
import com.icanpay.providers.UnionPayGateway;
import com.icanpay.providers.WeChatPaymentGataway;
import com.icanpay.utils.MatrixToImageWriter;
import com.icanpay.utils.Utility;

/**
 * 设置需要支付的订单的数据，创建支付订单URL地址或HTML表单
 * 
 * @author milanyangbo
 *
 */
public class PaymentSetting {
	GatewayBase gateway;
	Merchant merchant;
	Order order;
	boolean canQueryNotify;
	boolean canQueryNow;
	boolean canBuildAppParams;
	boolean canRefund;

	public PaymentSetting(GatewayType gatewayType) {
		gateway = createGateway(gatewayType);
	}

	public PaymentSetting(GatewayType gatewayType, Merchant merchant,
			Order order) {
		this(gatewayType);
		gateway.setMerchant(merchant);
		gateway.setOrder(order);

	}

	public GatewayBase getGateway() {
		return gateway;
	}

	public Merchant getMerchant() {
		return gateway.getMerchant();
	}

	public Order getOrder() {
		return gateway.getOrder();
	}

	public boolean isCanQueryNotify() {
		if (gateway instanceof QueryUrl || gateway instanceof QueryForm) {
			return true;
		}
		return false;
	}

	public boolean isCanQueryNow() {
		return gateway instanceof QueryNow;
	}

	public boolean isCanRefund() {
		return gateway instanceof RefundReq;
	}

	private GatewayBase createGateway(GatewayType gatewayType) {
		switch (gatewayType) {
		case Alipay: {
			return new AlipayGateway();
		}
		case WeChatPayment: {
			return new WeChatPaymentGataway();
		}
		case UnionPay: {
			return new UnionPayGateway();
		}
		default: {
			return new NullGateway();
		}
		}
	}

	/**
	 * 创建订单的支付Url、Form表单、二维码。 如果创建的是订单的Url或Form表单将跳转到相应网关支付，如果是二维码将输出二维码图片。
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void payment() throws IOException, Exception {
		HttpServletResponse response = Utility.getHttpServletResponse();
		response.setCharacterEncoding(gateway.getCharset());
		if (gateway instanceof PaymentUrl) {
			PaymentUrl paymentUrl = (PaymentUrl) gateway;
			response.sendRedirect(paymentUrl.buildPaymentUrl());
			return;
		}

		if (gateway instanceof PaymentForm) {
			PaymentForm paymentForm = (PaymentForm) gateway;
			response.getWriter().write(paymentForm.buildPaymentForm());
			return;
		}

		if (gateway instanceof PaymentQRCode) {
			PaymentQRCode paymentQRCode = (PaymentQRCode) gateway;
			buildQRCodeImage(paymentQRCode.getPaymentQRCodeContent());
			return;
		}

		throw new NotImplementedException(gateway.getGatewayType()
				+ " 没有实现支付接口");
	}

	/**
	 * WAP支付
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void wapPayment(Map<String, String> map) throws Exception {
		HttpServletResponse response = Utility.getHttpServletResponse();
		response.setCharacterEncoding(gateway.getCharset());
		if (gateway instanceof WapPaymentUrl) {
			WapPaymentUrl paymentUrl = (WapPaymentUrl) gateway;
			if (gateway.getGatewayType() == GatewayType.WeChatPayment) {
				response.getWriter()
						.write(String
								.format("<script language='javascript'>window.location='%s'</script>",
										paymentUrl.buildWapPaymentUrl(map)));
			} else {
				response.sendRedirect(paymentUrl.buildWapPaymentUrl(map));
			}
			return;
		}

		if (gateway instanceof WapPaymentForm) {
			WapPaymentForm paymentForm = (WapPaymentForm) gateway;
			response.getWriter().write(paymentForm.buildWapPaymentForm());
			return;
		}

		throw new NotImplementedException(gateway.getGatewayType()
				+ " 没有实现支付接口");
	}

	/**
	 * 查询订单，订单的查询通知数据通过跟支付通知一样的形式反回。用处理网关通知一样的方法接受查询订单的数据。
	 * 
	 * @throws Exception
	 */
	public void queryNotify() throws Exception {
		HttpServletResponse response = Utility.getHttpServletResponse();
		response.setCharacterEncoding(gateway.getCharset());

		if (gateway instanceof QueryUrl) {
			QueryUrl queryUrl = (QueryUrl) gateway;
			response.sendRedirect(queryUrl.buildQueryUrl());
			return;
		}

		if (gateway instanceof QueryForm) {
			QueryForm queryForm = (QueryForm) gateway;
			response.sendRedirect(queryForm.buildQueryForm());
			return;
		}

		throw new NotImplementedException(gateway.getGatewayType()
				+ " 没有实现 QueryUrl 或 QueryForm 查询接口");
	}

	/**
	 * 查询订单，立即获得订单的查询结果
	 * 
	 * @param productSet
	 * @return
	 * @throws Exception
	 */
	public boolean queryNow(ProductSet productSet) throws Exception {
		if (productSet == null) {
			productSet = ProductSet.APP;
		}

		if (gateway instanceof QueryNow) {
			QueryNow queryNow = (QueryNow) gateway;
			return queryNow.queryNow(productSet);
		}

		throw new NotImplementedException(gateway.getGatewayType()
				+ " 没有实现 QueryNow 查询接口");
	}

	/**
	 * 创建APP端SDK支付需要的参数
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> buildPayParams() throws Exception {
		if (gateway instanceof AppParams) {
			AppParams appParams = (AppParams) gateway;
			return appParams.buildPayParams();
		}

		throw new NotImplementedException(gateway.getGatewayType()
				+ " 没有实现 AppParams 查询接口");
	}

	/**
	 * 创建退款
	 * 
	 * @param refund
	 * @return
	 * @throws Exception
	 */
	public Refund buildRefund(Refund refund) throws Exception {
		if (gateway instanceof RefundReq) {
			RefundReq appParams = (RefundReq) gateway;
			return appParams.buildRefund(refund);
		}

		throw new NotImplementedException(gateway.getGatewayType()
				+ " 没有实现 RefundReq 查询接口");
	}

	/**
	 * 查询退款结果
	 * 
	 * @param refund
	 * @return
	 * @throws Exception
	 */

	public Refund buildRefundQuery(Refund refund) throws Exception {
		if (gateway instanceof RefundReq) {
			RefundReq appParams = (RefundReq) gateway;
			return appParams.buildRefundQuery(refund);
		}

		throw new NotImplementedException(gateway.getGatewayType()
				+ " 没有实现 RefundReq 查询接口");
	}

	/**
	 * 设置网关的数据
	 * 
	 * @param gatewayParameterName
	 * @param gatewayParameterValue
	 */
	public void setGatewayParameterValue(String gatewayParameterName,
			String gatewayParameterValue) {
		gateway.setGatewayParameterValue(gatewayParameterName,
				gatewayParameterValue);
	}

	/**
	 * 生成并输出二维码图片
	 * 
	 * @param paymentQRCodeContent
	 * @throws IOException
	 * @throws WriterException
	 */
	private void buildQRCodeImage(String paymentQRCodeContent)
			throws IOException, WriterException {
		HttpServletResponse response = Utility.getHttpServletResponse();
		response.setContentType("image/x-png");
		// TODO Auto-generated method stub
		int width = 300; // 二维码图片宽度
		int height = 300; // 二维码图片高度
		String format = "png";// 二维码的图片格式

		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, gateway.getCharset()); // 内容所使用字符集编码

		BitMatrix bitMatrix = new MultiFormatWriter().encode(
				paymentQRCodeContent, BarcodeFormat.QR_CODE, width, height,
				hints);
		// 生成二维码

		MatrixToImageWriter.writeToStream(bitMatrix, format,
				response.getOutputStream());
	}
}
