package com.bstek.dorado.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ConfigUtils;
import com.bstek.dorado.config.ParseContext;

/**
 * 不做任何实际处理的空属性解析器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 25, 2008
 */
public class IgnoreParser implements XmlParser {

	public Object parse(Node node, ParseContext context) throws Exception {
		return ConfigUtils.IGNORE_VALUE;
	}

}
