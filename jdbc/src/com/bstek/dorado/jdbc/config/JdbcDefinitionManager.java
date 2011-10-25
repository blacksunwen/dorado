package com.bstek.dorado.jdbc.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Element;

import com.bstek.dorado.config.definition.DefaultDefinitionManager;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.config.xml.JdbcParseContext;
import com.bstek.dorado.jdbc.config.xml.JdbcXmlConstants;
import com.bstek.dorado.jdbc.model.DbElementDefinition;

/**
 * JDBC模块的{@link com.bstek.dorado.config.definition.Definition}管理器
 * @author mark
 *
 */
public class JdbcDefinitionManager extends DefaultDefinitionManager<DbElementDefinition> {

	private Map<String, XmlElementWrapper> dbElementNodeMap = new LinkedHashMap<String, XmlElementWrapper>();
	
	public void reset(Map<String, XmlElementWrapper> dbElementNodeMap) {
		this.dbElementNodeMap = dbElementNodeMap;
		this.clearAllDefinitions();
	}
	
	@Override
	public DbElementDefinition getDefinition(String name) {
		DbElementDefinition def = super.getDefinition(name);
		if (def == null) {
			XmlElementWrapper wrapper = dbElementNodeMap.remove(name);
			if (wrapper != null) {
				Element node = wrapper.getElement();
				String envName = node.getAttribute(JdbcXmlConstants.ATTRIBUTE_ENVIROMENT);
				JdbcEnviroment env = JdbcUtils.getEnviromentManager().getEnviroment(envName);
				try {
					JdbcParseContext parseContext = new JdbcParseContext(env);
					parseContext.setResource(wrapper.getResource());
					
					ObjectParser parser = wrapper.getParser();
					def = (DbElementDefinition)parser.parse(node, parseContext);
					this.registerDefinition(name, def);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		return def;
	}

}
