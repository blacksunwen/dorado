package com.bstek.dorado.spring;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-20
 */
public interface DoradoAppContextImporter {
	void importDoradoAppContext(Element element, ParserContext parserContext)
			throws Exception;
}
