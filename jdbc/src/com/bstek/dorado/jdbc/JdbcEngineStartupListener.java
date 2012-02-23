package com.bstek.dorado.jdbc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserHelper;
import com.bstek.dorado.config.xml.XmlParserHelper.XmlParserInfo;
import com.bstek.dorado.core.EngineStartupListener;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.data.config.DataConfigEngineStartupListener;
import com.bstek.dorado.jdbc.config.DbModel;
import com.bstek.dorado.jdbc.config.DbmManager;
import com.bstek.dorado.jdbc.config.GlobalDbModelConfig;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;
import com.bstek.dorado.jdbc.config.JdbcParseContext;

/**
 * JDBC模块的启动器
 * 
 * @author mark
 * 
 */
public class JdbcEngineStartupListener extends EngineStartupListener implements
		ApplicationContextAware {

	private static Log logger = LogFactory.getLog(JdbcEngineStartupListener.class);
	
	private ApplicationContext ctx;

	private XmlParserHelper xmlParserHelper;
	private XmlDocumentBuilder xmlDocumentBuilder;
	
	public XmlParserHelper getXmlParserHelper() {
		return xmlParserHelper;
	}

	public void setXmlParserHelper(XmlParserHelper xmlParserHelper) {
		this.xmlParserHelper = xmlParserHelper;
	}

	public XmlDocumentBuilder getXmlDocumentBuilder() {
		return xmlDocumentBuilder;
	}

	public void setXmlDocumentBuilder(XmlDocumentBuilder xmlDocumentBuilder) {
		this.xmlDocumentBuilder = xmlDocumentBuilder;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
		/*
		 * JDBC模块一定是在DataType被加载之后启动（由于JdbcType的缘故）
		 */
		DataConfigEngineStartupListener l = applicationContext.getBean(DataConfigEngineStartupListener.class);
		this.setOrder(l.getOrder() + 10);
	}

	@Override
	public void onStartup() throws Exception {
		this.registerEnviroments();
		this.registerDbModels();
	}

	/**
	 * 注册{@link JdbcEnviroment}
	 */
	protected void registerEnviroments() {
		JdbcEnviromentManager manager = JdbcUtils.getEnviromentManager();
		Map<String, JdbcEnviroment> envMap = ctx.getBeansOfType(JdbcEnviroment.class);
		for (JdbcEnviroment env: envMap.values()) {
			manager.register(env);
		}
	}
	
	/**
	 * 注册{@link JdbcEnviroment}
	 * @throws Exception
	 */
	protected void registerDbModels() throws Exception {
		Resource[] resources = getDbmResources();
		
		if (logger.isInfoEnabled()) {
			String msg = "dbm already found: [";
			List<String> resourceNames = new ArrayList<String>(resources.length);
			for (Resource resource: resources) {
				resourceNames.add(resource.getPath());
			}
			
			msg += StringUtils.join(resourceNames, ',');
			msg += "]";
			logger.info(msg);
		}
		
		DbmManager configManager = JdbcUtils.getDbmManager();
		XmlParser parser = getDbmParser();
		for (Resource resource: resources) {
			Document document = getXmlDocumentBuilder().loadDocument(resource);
			Element documentElement = document.getDocumentElement();
			
			JdbcParseContext parseContext = new JdbcParseContext();
			parseContext.setResource(resource);
			DbModel dbm = (DbModel) parser.parse(documentElement, parseContext);
			
			configManager.registerDbm(dbm);
		}
	}
	
	/**
	 * 获取DBM的资源对象
	 * @return
	 * @throws Exception
	 */
	protected Resource[] getDbmResources() throws Exception {
		Map<String, GlobalDbModelConfig> configMap = ctx.getBeansOfType(GlobalDbModelConfig.class);
		Set<String> locationSet = new HashSet<String>();
		for (GlobalDbModelConfig config: configMap.values()) {
			locationSet.addAll(config.getConfigLocations());
		}
		String[] locations = locationSet.toArray(new String[locationSet.size()]);
		Resource[] resources = ResourceUtils.getResources(locations);
		
		return resources;
	}
	
	protected XmlParser getDbmParser() throws Exception {
		XmlParserInfo xmlParserInfo = getXmlParserHelper().getXmlParserInfos(DbModel.class).get(0);
		XmlParser parser = xmlParserInfo.getParser();
		return parser;
	}
}
