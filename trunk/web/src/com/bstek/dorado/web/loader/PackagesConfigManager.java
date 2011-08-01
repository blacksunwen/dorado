package com.bstek.dorado.web.loader;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;

/**
 * 资源包配置的管理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 23, 2008
 */
public class PackagesConfigManager {
	private XmlDocumentBuilder xmlDocumentBuilder;
	private XmlParser xmlParser;
	private List<String> configLocations = new ArrayList<String>();
	private PackagesConfig packagesConfig;

	/**
	 * 设置XML配置文件读取实现类。
	 */
	public void setXmlDocumentBuilder(XmlDocumentBuilder xmlDocumentBuilder) {
		this.xmlDocumentBuilder = xmlDocumentBuilder;
	}

	/**
	 * 设置用于完成XML解析的解析器。
	 */
	public void setXmlParser(XmlParser xmlParser) {
		this.xmlParser = xmlParser;
	}

	/**
	 * 设置要装载的资源包配置文件。
	 */
	public void setConfigLocations(List<String> configLocations) {
		this.configLocations.clear();
		this.configLocations.addAll(configLocations);
	}

	/**
	 * 添加一个资源包配置文件。
	 */
	public void addConfigLocation(String configLocation) {
		this.configLocations.add(configLocation);
	}

	/**
	 * 返回资源包配置信息对象。
	 * 
	 * @throws Exception
	 */
	public synchronized PackagesConfig getPackagesConfig() throws Exception {
		if (packagesConfig == null) {
			if (configLocations != null) {
				String[] locations = new String[configLocations.size()];
				configLocations.toArray(locations);
				packagesConfig = loadConfigs(ResourceUtils
						.getResources(locations));
			} else {
				packagesConfig = new PackagesConfig();
			}
		}
		return packagesConfig;
	}

	/**
	 * 装载一组资源包配置文件。
	 * 
	 * @throws Exception
	 */
	protected PackagesConfig loadConfigs(Resource[] resources) throws Exception {
		PackagesConfig importConfig = new PackagesConfig();
		PackagesConfigParseContext context = new PackagesConfigParseContext(
				importConfig);
		for (Resource resource : resources) {
			loadConfig(resource, context);
		}
		return importConfig;
	}

	/**
	 * 装载一个资源包配置文件。
	 * 
	 * @throws Exception
	 */
	protected void loadConfig(Resource resource,
			PackagesConfigParseContext context) throws Exception {
		Document document = xmlDocumentBuilder.loadDocument(resource);
		xmlParser.parse(document.getDocumentElement(), context);
	}
}
