package com.bstek.dorado.jdbc.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserHelper;
import com.bstek.dorado.config.xml.XmlParserHelper.XmlParserInfo;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.data.config.definition.PropertyDefDefinition;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.DefaultEntityDataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.jdbc.ModelStrategy;
import com.bstek.dorado.jdbc.model.table.TableDefinition;

/**
 * 默认的模型管理器
 * 
 * @author mark.li@bstek.com
 *
 */
public class DefaultDmbManager extends AbstractDbmManager {
	private static Log logger = LogFactory.getLog(DefaultDmbManager.class);
	
	private XmlParserHelper xmlParserHelper;
	private XmlDocumentBuilder xmlDocumentBuilder;
	private ModelStrategy modelStrategy;
	private DataTypeManager dataTypeManager;
	
	private boolean onRefresh = false;
	
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

	public void setDataTypeManager(DataTypeManager dataTypeManager) {
		this.dataTypeManager = dataTypeManager;
	}
	
	public ModelStrategy getModelStrategy() {
		return modelStrategy;
	}

	public DataTypeManager getDataTypeManager() {
		return dataTypeManager;
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
	
	private boolean isDebug() {
		return "debug".equalsIgnoreCase(Configure.getString("core.runMode"));
	}

	@Override
	synchronized
	protected void doRefresh() throws Exception {
		onRefresh = true;
		try {
			this.clearAllDefinitions();
			
			Resource[] resources = getDbmResources();
			this.logResources(resources);
			this.register(resources);
			this.doAfterRegister();
		} finally {
			onRefresh = false;
		}
	}
	
	protected void doAfterRegister() throws Exception{
		this.doAutoCreate();
	}

	protected void doAutoCreate(AbstractDbTableDefinition def) throws Exception {
		if (def.isAutoCreateDataType()) {
			this.createDataType(def);
		}
	}
	
	protected void createDataType(AbstractDbTableDefinition tableDef) throws Exception {
		String dataTypeName = tableDef.getName();
		DataType alreadyType = dataTypeManager.getDataType(dataTypeName);
		
		if (alreadyType == null) {
			DataTypeDefinitionManager manager = dataTypeManager.getDataTypeDefinitionManager();
			DataTypeDefinition def = modelStrategy.createDataTypeDefinition(tableDef);
			def.setImpl(DefaultEntityDataType.class.getName());
			def.setName(dataTypeName);
			manager.registerDefinition(dataTypeName, def);
			
			if (logger.isInfoEnabled()) {
				logger.info("** auto create datatype [" + dataTypeName + "]");
			}
		} else {
			if (alreadyType instanceof EntityDataType) {
				EntityDataType dataType = (EntityDataType)alreadyType;
				if (dataType.isAutoCreatePropertyDefs()) {
					DataTypeDefinitionManager dataTypeDefManager = dataTypeManager.getDataTypeDefinitionManager();
					DataTypeDefinition def = dataTypeDefManager.getDefinition(dataTypeName);
					
					boolean isCache = def.isCacheCreatedObject();
					def.setCacheCreatedObject(false);
					DataTypeDefinition def2 = modelStrategy.createDataTypeDefinition(tableDef);
					Map<String, ObjectDefinition> propertyDefMap = def2.getPropertyDefs();
					if (propertyDefMap != null && !propertyDefMap.isEmpty()) {
						for (ObjectDefinition objDef: propertyDefMap.values()) {
							PropertyDefDefinition pdef = (PropertyDefDefinition)objDef;
							String propertyName = (String)pdef.getProperty("name");
							
							if (def.getPropertyDef(propertyName) == null) {
								def.addPropertyDef(propertyName, pdef);
							}
						}
					}
					
					def.setCacheCreatedObject(isCache);
				}
			}
		}
	}

	/**
	 * 获取DBM的资源对象
	 * @return
	 * @throws Exception
	 */
	private Resource[] getDbmResources() throws Exception {
		GlobalDbModelConfig[] configs = getConfigs();
		if (configs == null || configs.length == 0) {
			return new Resource[0];
		} else {
			Set<String> locationSet = new HashSet<String>(configs.length);
			for (GlobalDbModelConfig config: configs) {
				locationSet.addAll(config.getConfigLocations());
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
	
	private void logResources(Resource[] resources) {
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
	}
	
	private void register(Resource[] resources) throws Exception {
		XmlParser parser = getDbmParser();
		for (Resource resource: resources) {
			Document document = getXmlDocumentBuilder().loadDocument(resource);
			Element documentElement = document.getDocumentElement();
			
			JdbcParseContext parseContext = new JdbcParseContext();
			parseContext.setResource(resource);
			DbModel dbm = (DbModel) parser.parse(documentElement, parseContext);
			
			this.register(dbm);
		}
	}
	
	private void doAutoCreate() throws Exception {
		Collection<DbElementDefinition> defs = this.getDefinitions().values();
		List<AbstractDbTableDefinition> otherTableDefs = new ArrayList<AbstractDbTableDefinition>();
		for (DbElementDefinition def: defs) {
			if (def instanceof AbstractDbTableDefinition) {
				if (def instanceof TableDefinition) {
					this.doAutoCreate((TableDefinition)def);
				} else {
					otherTableDefs.add((AbstractDbTableDefinition)def);
				}
			}
		}
		
		for (AbstractDbTableDefinition def: otherTableDefs) {
			this.doAutoCreate(def);
		}
	}
}
