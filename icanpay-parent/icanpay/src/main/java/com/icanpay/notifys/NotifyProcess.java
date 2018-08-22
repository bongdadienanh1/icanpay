package com.icanpay.notifys;

import com.icanpay.enums.GatewayParameterRequestMethod;
import com.icanpay.exceptions.GatewayException;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.GatewayParameter;
import com.icanpay.providers.NullGateway;
import com.icanpay.utils.Utility;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 网关通知的处理类，通过对返回数据的分析识别网关类型
 *
 * @author milanyangbo
 *
 */
public class NotifyProcess {

	Set<BaseGatewayVerify> baseGatewayVerifys;

	public Set<BaseGatewayVerify> getBaseGatewayVerifys() {
		return baseGatewayVerifys;
	}

	public void setBaseGatewayVerifys(Set<BaseGatewayVerify> baseGatewayVerifys) {
		this.baseGatewayVerifys = baseGatewayVerifys;
	}

	/**
	 * 验证网关的类型
	 *
	 * @return
	 * @throws Exception
	 */
	public GatewayBase getGateway() {
		List<GatewayParameter> gatewayParameterData = readNotifyData();
		for (BaseGatewayVerify verify : baseGatewayVerifys) {
			GatewayBase gatewayBase = verify.verify(gatewayParameterData);
			if (gatewayBase != null)
				return gatewayBase;
			continue;
		}

		return new NullGateway(gatewayParameterData);
	}

	/**
	 * 读取网关发回的数据。Get方式传入QueryString的值均为未解码
	 *
	 * @return
	 * @throws Exception
	 */
	private List<GatewayParameter> readNotifyData() {
		// TODO Auto-generated method stub
		List<GatewayParameter> gatewayParameters = new ArrayList<GatewayParameter>();
		readQueryString(gatewayParameters);
		readForm(gatewayParameters);
		readWechatpayXml(gatewayParameters);
		return gatewayParameters;
	}

	/**
	 * 设置网关的数据
	 *
	 * @param gatewayParameterList
	 *            保存网关参数的集合
	 * @param gatewayParameterName
	 *            网关的参数名称
	 * @param gatewayParameterValue
	 *            网关的参数值
	 * @param gatewayParameterRequestMethod
	 *            网关的参数的请求方式的类型
	 */
	private void setGatewayParameterValue(List<GatewayParameter> gatewayParameterList, String gatewayParameterName, String gatewayParameterValue,
			GatewayParameterRequestMethod gatewayParameterRequestMethod) {

		GatewayParameter existsParam = gatewayParameterList.stream().filter(p -> p.getName().equals(gatewayParameterName)).findFirst().orElse(null);

		if (existsParam == null) {
			GatewayParameter param = new GatewayParameter(gatewayParameterName, gatewayParameterValue, gatewayParameterRequestMethod);
			gatewayParameterList.add(param);
		} else {
			if (existsParam.getValue().equals(gatewayParameterValue)) {
				existsParam.setRequestMethod(GatewayParameterRequestMethod.Both);
			} else {
				existsParam.setRequestMethod(gatewayParameterRequestMethod);
				existsParam.setValue(gatewayParameterValue);
			}
		}
	}

	/**
	 * 读取GET提交的查询字符串中的数据
	 *
	 * @param gatewayParameterList
	 */
	private void readQueryString(List<GatewayParameter> gatewayParameterList) {
		// TODO Auto-generated method stub
		HttpServletRequest request = Utility.getHttpServletRequest();
		String queryString = request.getQueryString();
		String[] kvs = queryString.split("&");
		for (String kv : kvs) {
			String[] tmp = kv.split("=");
			if (tmp.length >= 2) {
				setGatewayParameterValue(gatewayParameterList, tmp[0], tmp[1], GatewayParameterRequestMethod.Get);
			}
		}
	}

	/**
	 * 读取POST提交的Form表单的数据
	 *
	 * @param gatewayParameterList
	 */
	private void readForm(List<GatewayParameter> gatewayParameterList) {
		// TODO Auto-generated method stub
		HttpServletRequest request = Utility.getHttpServletRequest();
		Map<String, String[]> params = request.getParameterMap();
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				setGatewayParameterValue(gatewayParameterList, key, value, GatewayParameterRequestMethod.Post);
			}
		}
	}

	/**
	 * 读取微信支付的通知
	 *
	 * @param gatewayParameterList
	 * @throws Exception
	 */
	private void readWechatpayXml(List<GatewayParameter> gatewayParameterList) {
		// TODO Auto-generated method stub

		if (isWechatpayNotify()) {
			String resultXml = null;
			try {
				resultXml = new String(IOUtils.toByteArray(Utility.getHttpServletRequest().getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new GatewayException(e.getMessage(), e);
			}

			Utility.xmlToMap(resultXml).forEach((key, val) -> {
				setGatewayParameterValue(gatewayParameterList, key, val, GatewayParameterRequestMethod.Post);
			});
		}
	}

	/**
	 * 是否是微信支付的通知
	 *
	 * @return
	 */
	private boolean isWechatpayNotify() {
		HttpServletRequest request = Utility.getHttpServletRequest();
		String requestType = request.getMethod();
		String contentType = request.getContentType();
		String userAgent = request.getHeader("User-Agent");
		if (requestType.equalsIgnoreCase("POST") && contentType.equalsIgnoreCase("text/xml") && userAgent.equalsIgnoreCase("Mozilla/4.0")) {
			return true;
		}
		return false;
	}
}
