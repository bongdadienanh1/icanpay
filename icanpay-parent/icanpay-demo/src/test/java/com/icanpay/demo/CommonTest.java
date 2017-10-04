package com.icanpay.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.icanpay.properties.AlipayProperties;
import com.icanpay.properties.UnionPayProperties;
import com.icanpay.properties.WeChatPaymentProperties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonTest {

	@Autowired
	private AlipayProperties alipayProperties;

	@Autowired
	private WeChatPaymentProperties weChatPaymentProperties;

	@Autowired
	private UnionPayProperties unionPayProperties;

	@Test
	public void TestProperties() {
		String iString = "";
		iString = "1";
	}
}
