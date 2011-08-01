package com.bstek.dorado.view.widget;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.common.event.ClientEventSupported;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserAnnotationHelper;
import com.bstek.dorado.view.config.xml.ComponentParserDispatcher;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-28
 */
public class ViewXmlParserAnnotationHelper extends XmlParserAnnotationHelper {
	private static final String PROPERTY_PARSER = "dorado.dataPropertyParser";
	private static final String COMPONENT_PROPERTY_PARSER = "dorado.componentPropertyParser";

	private static final String OBJECT_PARSER = "dorado.prototype.genericObjectParser";
	private static final String COMPONENT_PARSER_DISPATCH = "dorado.prototype.componentParserDispatcher";
	private static final String CLIENT_EVENT_PARSER = "dorado.clientEventParser";

	private static final String CLIENT_EVENT = "ClientEvent";

	private Map<Class<?>, ObjectParser> objectParserCache = new HashMap<Class<?>, ObjectParser>();

	@Override
	public XmlParser getDefaultPropertyParser(Class<?> cl) {
		String parserId = (Component.class.isAssignableFrom(cl)) ? COMPONENT_PROPERTY_PARSER
				: PROPERTY_PARSER;
		return (XmlParser) getBeanFactory().getBean(parserId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public XmlParser getDefaultSubParser(Class<?> cl) {
		if (Component.class.isAssignableFrom(cl)) {
			ComponentParserDispatcher componentParserDispatcher = (ComponentParserDispatcher) getBeanFactory()
					.getBean(COMPONENT_PARSER_DISPATCH);
			componentParserDispatcher.setComponentType((Class<Component>) cl);
			return componentParserDispatcher;
		} else {
			if (Modifier.isAbstract(cl.getModifiers())) {
				return null;
			} else {
				ObjectParser parser = objectParserCache.get(cl);
				if (parser == null) {
					parser = (ObjectParser) getBeanFactory().getBean(
							OBJECT_PARSER);
					parser.setImpl(cl.getName());
					if (ClientEventSupported.class.isAssignableFrom(cl)) {
						parser.registerSubParser(
								CLIENT_EVENT,
								(XmlParser) getBeanFactory().getBean(
										CLIENT_EVENT_PARSER));
					}
					objectParserCache.put(cl, parser);
				}
				return parser;
			}
		}

	}
}
