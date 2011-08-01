package com.bstek.dorado.data.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlParseException;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 9, 2009
 */
public class ComplexDataTypeNameUnsupportedException extends XmlParseException {
	private static final long serialVersionUID = 6783743664393518931L;

	/**
	 * @param message
	 *            异常信息
	 */
	public ComplexDataTypeNameUnsupportedException(String message) {
		super(message);
	}

	/**
	 * @param message
	 *            异常信息
	 * @param context
	 *            解析上下文
	 */
	public ComplexDataTypeNameUnsupportedException(String message,
			ParseContext context) {
		super(message, context);
	}

	/**
	 * @param message
	 *            异常信息
	 * @param node
	 *            相关的XML节点
	 * @param context
	 *            解析上下文
	 */
	public ComplexDataTypeNameUnsupportedException(String message, Node node,
			ParseContext context) {
		super(message, node, context);
	}

}
