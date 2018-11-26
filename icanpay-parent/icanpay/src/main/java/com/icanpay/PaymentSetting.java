package com.icanpay;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.icanpay.enums.GatewayType;
import com.icanpay.exceptions.GatewayException;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.interfaces.*;
import com.icanpay.utils.MatrixToImageWriter;
import com.icanpay.utils.Utility;
import org.apache.commons.lang3.NotImplementedException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

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

	public static PaymentSetting buid(GatewayBase gateway) {
		return new PaymentSetting(gateway);
	}

	public PaymentSetting(GatewayBase gateway) {
		this.gateway = gateway;
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

	public PaymentSetting setOrder(Order order) {
		gateway.setOrder(order);
		return this;
	}

	public Map<String, String> payment(HashMap<String, String>... extraParams) {
		HashMap<String, String> _extraParams = extraParams != null && extraParams.length > 0 ? extraParams[0] : null;
		switch (gateway.getGatewayTradeType()) {
			case APP:
				return appPayment();
			case Wap:
				wapPayment(_extraParams);
				break;
			case Web:
				webPayment();
				break;
			case QRCode:
				qRCodePayment();
				break;
			case Public:
				break;
			case BarCode:
				break;
			case Applet:
				break;
			case None: {
				throw new NotImplementedException(gateway.getGatewayType() + " 没有实现+ " + gateway.getGatewayTradeType() + "接口");
			}
			default:
				break;
		}
		return null;
	}

	/**
	 * 创建订单的支付Url、Form表单、二维码。 如果创建的是订单的Url或Form表单将跳转到相应网关支付，如果是二维码将输出二维码图片。
	 */
	private void webPayment() {
		HttpServletResponse response = Utility.getHttpServletResponse();
		response.setCharacterEncoding(gateway.getCharset());
		if (gateway instanceof PaymentUrl) {
			PaymentUrl paymentUrl = (PaymentUrl) gateway;
			try {
				response.sendRedirect(paymentUrl.buildPaymentUrl());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new GatewayException(e.getMessage(), e);
			}
			return;
		}

		if (gateway instanceof PaymentForm) {
			PaymentForm paymentForm = (PaymentForm) gateway;
			try {
				response.getWriter().write(paymentForm.buildPaymentForm());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new GatewayException(e.getMessage(), e);
			}
			return;
		}
		throw new NotImplementedException(gateway.getGatewayType() + " 没有实现 PaymentUrl 或 PaymentForm 支付接口");
	}

	/**
	 * WAP支付
	 *
	 * @param map
	 */
	private void wapPayment(Map<String, String> map) {
		HttpServletResponse response = Utility.getHttpServletResponse();
		response.setCharacterEncoding(gateway.getCharset());
		if (gateway instanceof WapPaymentUrl) {
			WapPaymentUrl paymentUrl = (WapPaymentUrl) gateway;
			if (gateway.getGatewayType() == GatewayType.WeChatpay) {
				try {
					response.getWriter()
							.write(String.format("<script language='javascript'>window.location='%s'</script>", paymentUrl.buildWapPaymentUrl(map)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new GatewayException(e.getMessage(), e);
				}
			} else {
				try {
					response.sendRedirect(paymentUrl.buildWapPaymentUrl(map));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new GatewayException(e.getMessage(), e);
				}
			}
			return;
		}

		if (gateway instanceof WapPaymentForm) {
			WapPaymentForm paymentForm = (WapPaymentForm) gateway;
			try {
				response.getWriter().write(paymentForm.buildWapPaymentForm());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new GatewayException(e.getMessage(), e);
			}
			return;
		}
		throw new NotImplementedException(gateway.getGatewayType() + " 没有实现 WapPaymentUrl 或 WapPaymentForm 支付接口");
	}

	/**
	 * 二维码支付
	 */
	private void qRCodePayment() {
		// TODO Auto-generated method stub
		if (gateway instanceof PaymentQRCode) {
			PaymentQRCode paymentQRCode = (PaymentQRCode) gateway;
			try {
				buildQRCodeImage(paymentQRCode.getPaymentQRCodeContent());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new GatewayException(e.getMessage(), e);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				throw new GatewayException(e.getMessage(), e);
			}
			return;
		}
		throw new NotImplementedException(gateway.getGatewayType() + " 没有实现 PaymentQRCode 支付接口");
	}

	/**
	 * 创建APP端SDK支付需要的参数
	 * @return
	 */
	private Map<String, String> appPayment() {
		if (gateway instanceof PaymentApp) {
			PaymentApp paymentApp = (PaymentApp) gateway;
			return paymentApp.buildPayParams();
		}
		throw new NotImplementedException(gateway.getGatewayType() + " 没有实现 PaymentApp 支付接口");
	}

	/**
	 * 查询订单，订单的查询通知数据通过跟支付通知一样的形式反回。用处理网关通知一样的方法接受查询订单的数据。
	 */
	public void queryNotify() {
		HttpServletResponse response = Utility.getHttpServletResponse();
		response.setCharacterEncoding(gateway.getCharset());

		if (gateway instanceof QueryUrl) {
			QueryUrl queryUrl = (QueryUrl) gateway;
			try {
				response.sendRedirect(queryUrl.buildQueryUrl());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new GatewayException(e.getMessage(), e);
			}
			return;
		}

		if (gateway instanceof QueryForm) {
			QueryForm queryForm = (QueryForm) gateway;
			try {
				response.sendRedirect(queryForm.buildQueryForm());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new GatewayException(e.getMessage(), e);
			}
			return;
		}
		throw new NotImplementedException(gateway.getGatewayType() + " 没有实现 QueryUrl 或 QueryForm 查询接口");
	}

	/**
	 * 查询订单，立即获得订单的查询结果
	 * @return
	 */
	public boolean queryNow() {
		if (gateway instanceof QueryNow) {
			QueryNow queryNow = (QueryNow) gateway;
			return queryNow.queryNow();
		}
		throw new NotImplementedException(gateway.getGatewayType() + " 没有实现 QueryNow 查询接口");
	}

	/**
	 * 生成并输出二维码图片
	 *
	 * @param paymentQRCodeContent
	 * @throws IOException
	 * @throws WriterException
	 */
	private void buildQRCodeImage(String paymentQRCodeContent) throws IOException, WriterException {
		HttpServletResponse response = Utility.getHttpServletResponse();
		response.setContentType("image/x-png");
		// TODO Auto-generated method stub
		int width = 300; // 二维码图片宽度
		int height = 300; // 二维码图片高度
		String format = "png";// 二维码的图片格式

		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, gateway.getCharset()); // 内容所使用字符集编码

		BitMatrix bitMatrix = new MultiFormatWriter().encode(paymentQRCodeContent, BarcodeFormat.QR_CODE, width, height, hints);
		// 生成二维码
		MatrixToImageWriter.writeToStream(bitMatrix, format, response.getOutputStream());
	}
}
