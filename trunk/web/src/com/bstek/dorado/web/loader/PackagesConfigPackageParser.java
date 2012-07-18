package com.bstek.dorado.web.loader;

import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ConfigUtils;
import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ConfigurableDispatchableXmlParser;
import com.bstek.dorado.util.Assert;

/**
 * 资源包的解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 24, 2008
 */
public class PackagesConfigPackageParser extends
		ConfigurableDispatchableXmlParser {
	private static final String NONE_FILE = "(none)";

	@Override
	@SuppressWarnings("unchecked")
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Element element = (Element) node;
		String name = element.getAttribute("name");
		Assert.notEmpty(name);
		Package pkg = new Package(name);

		Map<String, Object> properties = parseProperties(element, context);
		if (!properties.containsKey("fileNames")) {
			Object value = parseProperty("fileNames", element, context);
			if (value != null && value != ConfigUtils.IGNORE_VALUE) {
				properties.put("fileNames", value);
			}
		}

		String fileNamesText = StringUtils.trim((String) properties
				.remove("fileNames"));
		fileNamesText = StringUtils.defaultIfEmpty(fileNamesText, NONE_FILE);
		pkg.setFileNames(fileNamesText.split(","));

		String dependsText = (String) properties.remove("depends");
		if (StringUtils.isNotEmpty(dependsText)) {
			String[] dependsArray = dependsText.split(",");
			for (String depends : dependsArray) {
				pkg.getDepends().add(depends);
			}
		}

		String dependedByText = (String) properties.remove("dependedBy");
		if (StringUtils.isNotEmpty(dependedByText)) {
			String[] dependedByArray = dependedByText.split(",");
			for (String dependedBy : dependedByArray) {
				pkg.getDependedBy().add(dependedBy);
			}
		}

		((Map<String, Object>) new BeanMap(pkg)).putAll(properties);
		return pkg;
	}

}
