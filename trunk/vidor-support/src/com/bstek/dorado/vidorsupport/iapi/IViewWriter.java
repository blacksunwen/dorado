package com.bstek.dorado.vidorsupport.iapi;

import java.io.OutputStream;

import com.bstek.dorado.idesupport.model.RuleSet;

/**
 * View Writer此接口主要为Cloudo客户端服务
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 */
public interface IViewWriter {

	/**
	 * 基于工程规则解析View
	 * 
	 * @param json
	 *            Json字符串
	 * @param output
	 *            View文件输出流
	 * @param encoding
	 *            文件编码
	 * @throws Exception
	 */
	public void write(String json, OutputStream output, String encoding)
			throws Exception;

	/**
	 * 基于规则解析输出View
	 * 
	 * @param json
	 *            Json字符串
	 * @param output
	 *            View文件输出流
	 * @param encoding
	 *            文件编码
	 * @param ruleSet
	 *            Dorado规则(class:com.bstek.dorado.idesupport.model.RuleSet)
	 * @throws Exception
	 */
	public void write(String json, OutputStream output, String encoding,
			RuleSet ruleSet) throws Exception;

	public String toXML(String json) throws Exception;

	public String toXML(String json, RuleSet ruleSet) throws Exception;
}
