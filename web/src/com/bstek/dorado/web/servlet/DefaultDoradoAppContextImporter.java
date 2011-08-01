package com.bstek.dorado.web.servlet;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

import com.bstek.dorado.spring.DoradoAppContextImporter;
import com.bstek.dorado.web.ConsoleUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-20
 */
public class DefaultDoradoAppContextImporter implements
		DoradoAppContextImporter {
	private static final Log logger = LogFactory
			.getLog(DefaultDoradoAppContextImporter.class);

	protected void importBeanDefinitionResource(String location,
			Element element, ParserContext parserContext) throws Exception {
		XmlReaderContext readerContext = parserContext.getReaderContext();
		try {
			ResourceLoader resourceLoader = readerContext.getResourceLoader();
			Resource relativeResource = resourceLoader.getResource(location);

			int importCount = readerContext.getReader().loadBeanDefinitions(
					relativeResource);
			if (logger.isDebugEnabled()) {
				logger.debug("Imported " + importCount
						+ " bean definitions from dorado-context [" + location
						+ "]");
			}
		} catch (Exception ex) {
			readerContext.error("Invalid dorado-context [" + location
					+ "] to import bean definitions from", element, null, ex);
		}

		readerContext.fireImportProcessed(location,
				readerContext.extractSource(element));
	}

	public void importDoradoAppContext(Element element,
			ParserContext parserContext) throws Exception {
		List<String> doradoContextLocations = DoradoLoader.getInstance()
				.getContextLocations(false);
		Assert.notNull("Can not get [doradoContextLocations], the DoradoPreloadListener may not configured or configured in wrong order. "
				+ "Please check your web.xml.");

		ConsoleUtils.outputLoadingInfo("Loading dorado context configures...");

		for (String location : doradoContextLocations) {
			importBeanDefinitionResource(location, element, parserContext);
		}
	}
}
