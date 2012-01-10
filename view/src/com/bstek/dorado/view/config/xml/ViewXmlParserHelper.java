package com.bstek.dorado.view.config.xml;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserHelper;
import com.bstek.dorado.util.clazz.TypeInfo;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.ComponentParser;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-12-1
 */
public class ViewXmlParserHelper extends XmlParserHelper {
	private static final String WILCARD = "*";
	private static final String ITEMS_PARSER = "dorado.itemsParser";
	private static final String COMPONENT_PARSER_DISPATCHER = "dorado.componentParserDispatcher";

	@Override
	protected void doInitObjectParser(Context context,
			ObjectParser objectParser, XmlNodeInfo xmlNodeInfo,
			Class<?> beanType) throws Exception {
		super.doInitObjectParser(context, objectParser, xmlNodeInfo, beanType);

		if (Component.class.isAssignableFrom(beanType)
				&& objectParser instanceof ComponentParser) {
			Widget widget = beanType.getAnnotation(Widget.class);
			if (widget != null) {
				((ComponentParser) objectParser)
						.setComponentType(widget.name());
			}
		}
	}

	@Override
	protected XmlParserInfo getPropertyXmlParserInfo(Context context,
			Class<?> beanType, String propertyName, TypeInfo typeInfo,
			XmlProperty xmlProperty) throws Exception {
		if (xmlProperty != null && typeInfo != null
				&& Object.class.equals(typeInfo.getType())
				&& typeInfo.isAggregated()) {
			XmlParser propertyParser = (XmlParser) beanFactory.getBean(
					ITEMS_PARSER, XmlParser.class);
			return new XmlParserInfo(propertyName, propertyParser);
		} else {
			return super.getPropertyXmlParserInfo(context, beanType,
					propertyName, typeInfo, xmlProperty);
		}
	}

	@Override
	protected List<XmlParserInfo> getSubNodeXmlParserInfosByPropertyType(
			Context context, Class<?> propertyType) throws Exception {
		if (Component.class.equals(propertyType)
				|| Control.class.equals(propertyType)) {
			List<XmlParserInfo> xmlParserInfos = new ArrayList<XmlParserInfo>();
			xmlParserInfos.add(new XmlParserInfo(WILCARD, beanFactory.getBean(
					COMPONENT_PARSER_DISPATCHER, XmlParser.class)));
			return xmlParserInfos;
		} else {
			return super.getSubNodeXmlParserInfosByPropertyType(context,
					propertyType);
		}
	}

}
