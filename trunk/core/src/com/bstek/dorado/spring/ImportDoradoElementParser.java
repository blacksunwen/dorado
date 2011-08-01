package com.bstek.dorado.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;

public class ImportDoradoElementParser implements BeanDefinitionParser {
	private static final Log logger = LogFactory
			.getLog(ImportDoradoElementParser.class);

	private static final String DEFAULT_IMPORTER = "com.bstek.dorado.web.servlet.DefaultDoradoAppContextImporter";

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		try {
			@SuppressWarnings("unchecked")
			Class<DoradoAppContextImporter> cl = (Class<DoradoAppContextImporter>) ClassUtils
					.forName(DEFAULT_IMPORTER, getClass().getClassLoader());
			DoradoAppContextImporter importer = cl.newInstance();
			importer.importDoradoAppContext(element, parserContext);
		}
		catch (Exception e) {
			logger.error(e, e);
		}
		return null;
	}

}
