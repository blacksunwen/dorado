package com.bstek.dorado.view.config.xml;

import org.apache.commons.lang.ClassUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlParseException;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegistry;
import com.bstek.dorado.view.widget.Component;

/**
 * Component解析任务的分配器。
 * <p>
 * 其作用是根据节点的属性将各种Component的解析任务分派给相应的子解析器。
 * </p>
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 22, 2008
 */
public class ComponentParserDispatcher implements XmlParser {
	private Class<Component> componentType;
	private ComponentTypeRegistry componentTypeRegistry;

	@SuppressWarnings("unchecked")
	public void setComponentTypeName(String componentTypeName)
			throws ClassNotFoundException {
		this.componentType = (Class<Component>) ClassUtils
				.getClass(componentTypeName);
	}

	public void setComponentType(Class<Component> componentType) {
		this.componentType = componentType;
	}

	public Class<Component> getComponentType() {
		return componentType;
	}

	/**
	 * 设置组件类型的注册管理器。
	 */
	public void setComponentTypeRegistry(
			ComponentTypeRegistry componentTypeRegistry) {
		this.componentTypeRegistry = componentTypeRegistry;
	}

	public Object parse(Node node, ParseContext context) throws Exception {
		ViewParseContext viewContext = (ViewParseContext) context;
		XmlParser parser = null;
		Element element = (Element) node;

		String type = element.getTagName();
		ComponentTypeRegisterInfo registerInfo = componentTypeRegistry
				.getRegisterInfo(type);
		if (registerInfo != null) {
			parser = (XmlParser) registerInfo.getParser();
		} else {
			throw new XmlParseException("Unrecognized Component type [" + type
					+ "].", element, context);
		}

		if (parser == null) {
			throw new XmlParseException(
					"Can not get Parser for DataProvider of [" + type
							+ "] type.", element, context);
		}

		ComponentTypeRegisterInfo originRegisterInfo = viewContext
				.getCurrentComponentTypeRegisterInfo();
		viewContext.setCurrentComponentTypeRegisterInfo(registerInfo);
		try {
			return parser.parse(element, context);
		} finally {
			viewContext.setCurrentComponentTypeRegisterInfo(originRegisterInfo);
		}
	}
}
