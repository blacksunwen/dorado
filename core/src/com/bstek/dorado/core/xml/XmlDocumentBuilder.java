package com.bstek.dorado.core.xml;

import org.w3c.dom.Document;

import com.bstek.dorado.core.io.Resource;

/**
 * XML读取工具类的通用接口。
 * <p>
 * 读取将给定的{@link com.bstek.dorado.core.io.Resource}中的XML资源解析为
 * {@link org.w3c.dom.Document}。
 * </p>
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 15, 2007
 * @see com.bstek.dorado.core.io.Resource
 * @see org.w3c.dom.Document
 */
public interface XmlDocumentBuilder {

	/**
	 * 创建一个空的XMLDocument对象。
	 * @return XML的Document对象。
	 * @throws Exception
	 */
	Document newDocument() throws Exception;

	/**
	 * 解析resource中的XML资源并返回DOM对象。
	 * @param resource 资源描述对象。
	 * @return XML的Document对象。
	 * @throws Exception
	 */
	Document loadDocument(Resource resource) throws Exception;

	/**
	 * 解析resource中的XML资源并返回DOM对象。
	 * @param resource 资源描述对象。
	 * @param charset 字符集。
	 * @return XML的Document对象。
	 * @throws Exception
	 */
	Document loadDocument(Resource resource, String charset) throws Exception;
}
