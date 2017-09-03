package com.icanpay.interfaces;

/**
 * 通过form表单提交查询订单
 * 
 * @author milanyangbo
 *
 */
public interface QueryForm {

	/**
	 * 创建包含查询订单数据的form表单的HTML代码
	 * 
	 * @return
	 */
	String buildQueryForm();
}
