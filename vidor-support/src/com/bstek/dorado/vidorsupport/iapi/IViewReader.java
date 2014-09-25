package com.bstek.dorado.vidorsupport.iapi;

import java.io.InputStream;

import com.bstek.dorado.idesupport.model.RuleSet;
/**
 * View Reader此接口主要为Cloudo客户端服务
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 */
public interface IViewReader {
	/**
	 * 基于工程规则解析View
	 * 
	 * @param input
	 *            View文件输入流
	 * @param encoding
	 *            文件编码
	 * @return Json字符串
	 * @throws Exception
	 */
	public String read(InputStream input, String encoding) throws Exception;

	/**
	 * 
	 * @param input
	 *            View文件输入流
	 * @param encoding
	 *            文件编码
	 * @param ruleSet
	 * 			  Dorado规则(class:com.bstek.dorado.idesupport.model.RuleSet)
	 * @return Json字符串
	 * @throws Exception
	 */
	public String read(InputStream input, String encoding, RuleSet ruleSet)
			throws Exception;
}
