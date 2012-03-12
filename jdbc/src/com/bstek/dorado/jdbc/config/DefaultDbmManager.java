package com.bstek.dorado.jdbc.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserHelper;
import com.bstek.dorado.config.xml.XmlParserHelper.XmlParserInfo;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.jdbc.JdbcIntercepter;
import com.bstek.dorado.jdbc.ModelStrategy;

/**
 * 默认的模型管理器
 * 
 * @author mark.li@bstek.com
 *
 */
public class DefaultDbmManager extends AbstractDbmManager {
	private XmlParserHelper xmlParserHelper;
	private XmlDocumentBuilder xmlDocumentBuilder;
	private ModelStrategy modelStrategy;
	private boolean onRefresh = false;
	private JdbcIntercepter interceper;
	
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

	public void setModelStrategy(ModelStrategy modelStrategy) {
		this.modelStrategy = modelStrategy;
	}
	
	public ModelStrategy getModelStrategy() {
		return modelStrategy;
	}

	public JdbcIntercepter getInterceper() {
		return interceper;
	}

	public void setInterceper(JdbcIntercepter interceper) {
		this.interceper = interceper;
	}

	@Override
	protected void register(DbElementDefinition def) {
		if (interceper != null) {
			def = interceper.getDefinition(def);
		}
		if (def != null) {
			super.register(def);
		}
	}

	@Override
	public DbElementDefinition getDefinition(String name) {
		if (!onRefresh && isDebug()) {
			try {
				this.doRefresh();
			} catch (Exception e) {
				throw new RuntimeException("Refresh dbm error.", e);
			}
		}
		return super.getDefinition(name);
	}
	
	private Boolean debug;
	private boolean isDebug() {
		if (debug == null) {
			debug = "debug".equalsIgnoreCase(Configure.getString("core.runMode"));
		}
		return debug;
	}

	@Override
	synchronized
	protected void doRefresh() throws Exception {
		onRefresh = true;
		try {
			this.clearAllDefinitions();
			
			Resource[] resources = getDbmResources();
			this.loadResources(resources);
		} finally {
			onRefresh = false;
		}
	}
	
	protected void onAfterLoadResources() throws Exception{
		this.doAutoCreate();
	}

	/**
	 * 获取DBM的资源对象
	 * @return
	 * @throws Exception
	 */
	private Resource[] getDbmResources() throws Exception {
		JdbcConfigLoader[] configs = getConfigs();
		if (configs == null || configs.length == 0) {
			return new Resource[0];
		} else {
			Set<String> locationSet = new HashSet<String>(configs.length);
			for (JdbcConfigLoader config: configs) {
				String c = config.getConfigLocation();
				if (StringUtils.isNotEmpty(c)) {
					locationSet.add(c);
				}
				
				List<String> cs = config.getConfigLocations();
				if (cs != null && cs.size() > 0) {
					locationSet.addAll(cs);
				}
			}
			String[] locations = locationSet.toArray(new String[locationSet.size()]);
			Resource[] resources = ResourceUtils.getResources(locations);
			
			return resources;
		}
	}
	
	private XmlParser getDbmParser() throws Exception {
		XmlParserInfo xmlParserInfo = getXmlParserHelper().getXmlParserInfos(DbModel.class).get(0);
		XmlParser parser = xmlParserInfo.getParser();
		return parser;
	}
	
	private void loadResources(Resource[] resources) throws Exception {
		XmlParser parser = getDbmParser();
		for (Resource resource: resources) {
			Document document = getXmlDocumentBuilder().loadDocument(resource);
			Element documentElement = document.getDocumentElement();
			
			JdbcParseContext parseContext = new JdbcParseContext();
			parseContext.setResource(resource);
			DbModel dbm = (DbModel) parser.parse(documentElement, parseContext);
			
			this.register(dbm);
		}
		
		this.onAfterLoadResources();
	}
	
	private void doAutoCreate() throws Exception {
		Collection<DbElementDefinition> defs = this.getDefinitions().values();
		for (DbElementDefinition def: defs) {
			if (def instanceof AbstractDbTableDefinition) {
				AbstractDbTableDefinition tableDef = (AbstractDbTableDefinition)def;
				if (tableDef.isAutoCreateDataType()) {
					modelStrategy.createDataType(tableDef);
				}
				if (tableDef.isAutoCreateDataProvider()) {
					modelStrategy.createDataProvider(tableDef);
				}
			}
		}
	}
}
