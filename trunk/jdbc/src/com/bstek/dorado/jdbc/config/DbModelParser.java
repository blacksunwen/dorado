package com.bstek.dorado.jdbc.config;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * {@link com.bstek.dorado.jdbc.config.DbModel}的解析器
 * 
 * @author mark.li@bstek.com
 *
 */
public class DbModelParser extends ObjectParser {

	private static final String ALL_ELEMENTS_ATTR = "ALL_DBELEMENTS";
	
	private JdbcEnviromentManager enviromentManager;
	
	public JdbcEnviromentManager getEnviromentManager() {
		return enviromentManager;
	}

	public void setEnviromentManager(JdbcEnviromentManager enviromentManager) {
		this.enviromentManager = enviromentManager;
	}

	@Override
	protected List<?> dispatchChildElements(Element element,
			ParseContext context) throws Exception {
		List<?> list = super.dispatchChildElements(element, context);
		context.getAttributes().put(ALL_ELEMENTS_ATTR, list);
		return list;
	}

	@Override
	protected Object dispatchElement(String pathPrefix, Element child,
			ParseContext context) throws Exception {
		Element dbModel = (Element)child.getParentNode();
		if (dbModel.hasAttribute(XmlConstants.JDBC_ENVIROMENT)) {
			child.setAttribute(XmlConstants.JDBC_ENVIROMENT, 
					dbModel.getAttribute(XmlConstants.JDBC_ENVIROMENT));
		}
		return super.dispatchElement(pathPrefix, child, context);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Element element = (Element)node;
		JdbcParseContext jdbcContext = (JdbcParseContext)context;
		if (element.hasAttribute(XmlConstants.JDBC_ENVIROMENT)) {
			String envName = element.getAttribute(XmlConstants.JDBC_ENVIROMENT);
			JdbcEnviroment jdbcEnviroment = enviromentManager.getEnviroment(envName);
			jdbcContext.setJdbcEnviroment(jdbcEnviroment);
		} else {
			jdbcContext.setJdbcEnviroment(enviromentManager.getDefault());
		}
		
		ObjectDefinition definition = (ObjectDefinition)super.doParse(element, context);
		DbModel dbModel = (DbModel)definition.create(new CreationContext());
		dbModel.setResource(context.getResource());
		
		List<DbElementDefinition> list = (List<DbElementDefinition>)context.getAttributes().get(ALL_ELEMENTS_ATTR);
		if (list != null) {
			dbModel.addAll(list);
		}
		
		return dbModel;
	}

}
