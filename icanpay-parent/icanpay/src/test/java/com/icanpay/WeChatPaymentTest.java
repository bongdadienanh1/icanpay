package com.icanpay;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import com.icanpay.utils.Utility;

public class WeChatPaymentTest {

	/**
	 * 将网关数据转换成XML
	 * 
	 * @throws Exception
	 */
	@Test
	public void ConvertGatewayParameterDataToXmlTest() throws Exception {
		SortedMap<String, String> sortedMap = new TreeMap<String, String>();
		sortedMap.put("c", "c");
		sortedMap.put("d", "d");
		sortedMap.put("w", "w");
		sortedMap.put("q", "q");
		sortedMap.put("h", "h");
		sortedMap.put("e", "e");
		System.out.print(Utility.mapToXml(sortedMap));
	}

}
