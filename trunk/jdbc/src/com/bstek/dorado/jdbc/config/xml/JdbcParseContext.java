package com.bstek.dorado.jdbc.config.xml;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.config.xml.XmlParseException;
import com.bstek.dorado.data.config.xml.NodeWrapper;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.JdbcEnviroment;

public class JdbcParseContext extends ParseContext {

	private Map<String, Map<String, NodeWrapper>> nodeMaps = new HashMap<String, Map<String, NodeWrapper>>();
	
	private ThreadLocal<JdbcEnviroment> LOCAL_JDBC_ENV = new ThreadLocal<JdbcEnviroment>();
	
	public Object parse(Element element, ParseCallback callback) throws Exception {
		try {
			String envName = element.getAttribute(JdbcXmlConstants.ATTRIBUTE_ENVIROMENT);
			JdbcEnviroment env = JdbcUtils.getEnviromentManager().getEnviroment(envName);
			LOCAL_JDBC_ENV.set(env);
			
			return callback.doParse(element, this);
		} finally {
			LOCAL_JDBC_ENV.remove();
		}
		
	}
	
	public JdbcEnviroment getJdbcEnviroment() {
		return LOCAL_JDBC_ENV.get(); 
	}
	
	public Map<String, NodeWrapper> getElements(String tagName) {
		Map<String, NodeWrapper> nodeMap = nodeMaps.get(tagName);
		if (nodeMap == null) {
			nodeMap = new HashMap<String, NodeWrapper>();
			nodeMaps.put(tagName, nodeMap);
		}
		return nodeMap;
	}
	
	public void putElement(Element element) {
		String tagName = element.getTagName();
		String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
		if (StringUtils.isEmpty(name)) {
			throw new XmlParseException("[" + XmlConstants.ATTRIBUTE_NAME + "] attribute can not be empty", element, this);
		}
		
		Map<String, NodeWrapper> nodeMap = nodeMaps.get(tagName);
		if (nodeMap == null) {
			nodeMap = new HashMap<String, NodeWrapper>();
			nodeMaps.put(tagName, nodeMap);
		}
		
		if (nodeMap.containsKey(name)) {
			boolean overwrite = BooleanUtils.toBoolean(element.getAttribute(JdbcXmlConstants.ATTRIBUTE_OVERWRITE));
			if (!overwrite) {
				throw new XmlParseException(tagName + " [" + name + "] is not unique!", element, this);
			}
		}
		nodeMap.put(name, new NodeWrapper(element, this.getResource()));
	}
	
	public boolean hasElement(String tagName, String name) {
		Map<String, NodeWrapper> nodeMap = nodeMaps.get(tagName);
		if (nodeMap != null) {
			return nodeMap.containsKey(name);
		} else {
			return false;
		}
	}
}
