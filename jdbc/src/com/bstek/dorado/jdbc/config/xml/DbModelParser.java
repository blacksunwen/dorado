package com.bstek.dorado.jdbc.config.xml;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.config.DbModel;
import com.bstek.dorado.jdbc.model.DbElementCreationContext;
import com.bstek.dorado.jdbc.model.DbElementDefinition;

public class DbModelParser extends ObjectParser {

	private static final String ALL_ELEMENTS_ATTR = "allDbElements";
	
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
		Element parent = (Element)child.getParentNode();
		if (parent.hasAttribute(XmlConstants.JDBC_ENVIROMENT)) {
			child.setAttribute(XmlConstants.JDBC_ENVIROMENT, parent.getAttribute(XmlConstants.JDBC_ENVIROMENT));
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
			JdbcEnviroment jdbcEnviroment = JdbcUtils.getEnviromentManager().getEnviroment(envName);
			jdbcContext.setJdbcEnviroment(jdbcEnviroment);
		} else {
			jdbcContext.setJdbcEnviroment(JdbcUtils.getEnviromentManager().getDefault());
		}
		
		ObjectDefinition definition = (ObjectDefinition)super.doParse(element, context);
		DbElementCreationContext createContext = new DbElementCreationContext();
		
		DbModel dbModel = (DbModel)definition.create(createContext);
		dbModel.setResource(context.getResource());
		
		List<DbElementDefinition> list = (List<DbElementDefinition>)context.getAttributes().get(ALL_ELEMENTS_ATTR);
		if (list != null) {
			dbModel.addAll(list);
		}
		
		return dbModel;
	}

}
