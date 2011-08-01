package com.bstek.dorado.config.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ConfigUtils;
import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.core.el.ExpressionHandler;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * XML分派解析器的抽象类。<br>
 * 可支持将XML节点中各个属性和子节点的解析任务分派到相应子解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 20, 2007
 */
public abstract class DispatchableXmlParser implements XmlParser {
	public static final String SELF = "#self";
	public static final char SUB_PARSER_PATH_SEPERATOR = '/';

	/**
	 * 默认解析器的约束条件。<br>
	 * 该约束条件表示如果根据某约束条件无法找到匹配的解析器，那么就使用与{@link #WILDCARD}相关的解析器。
	 */
	public static final String WILDCARD = "*";

	private Map<String, XmlParser> propertyParsers = new LinkedHashMap<String, XmlParser>();
	private Map<String, XmlParser> subParsers = new LinkedHashMap<String, XmlParser>();

	/**
	 * 返回EL表达式的处理器。
	 */
	protected abstract ExpressionHandler getExpressionHandler();

	/**
	 * 向当前解析器中注册一个属性解析器。
	 * 
	 * @param constraint
	 *            要注册的属性解析器的约束条件。如果此参数的值为null，那么系统将使用{@link #WILDCARD}
	 *            作为内部使用的约束条件。
	 * @param parser
	 *            属性解析器
	 */
	public void registerPropertyParser(String constraint, XmlParser parser) {
		Assert.notNull(parser, "[parser] is required");
		propertyParsers.put(constraint, parser);
	}

	/**
	 * 返回所有已注册的属性解析器。其中Map的键值是约束条件，值是属性解析器。
	 * 
	 * @return 属性解析器的Map集合。
	 */
	public Map<String, XmlParser> getPropertyParsers() {
		return propertyParsers;
	}

	/**
	 * 根据约束条件返回一个匹配的属性解析器。
	 * 
	 * @param constraint
	 *            约束条件。 如果无法直接通过约束条件找到一个匹配的子解析器，那么此方法将尝试返回WILDCARD相关的子解析器。
	 */
	protected XmlParser findPropertyParser(String constraint) {
		constraint = StringUtils.defaultString(constraint, WILDCARD);
		XmlParser parser = propertyParsers.get(constraint);
		if (parser == null && !WILDCARD.equals(constraint)) {
			parser = propertyParsers.get(WILDCARD);
		}
		return parser;
	}

	/**
	 * 向当前解析器中注册一个子解析器。
	 * 
	 * @param constraint
	 *            要注册的子解析器的约束条件。如果此参数的值为null，那么系统将使用WILDCARD作为内部使用的约束条件。
	 * @param parser
	 *            子解析器
	 */
	public void registerSubParser(String constraint, XmlParser parser) {
		Assert.notNull(parser, "[parser] is required");
		subParsers.put(constraint, parser);
	}

	/**
	 * 返回所有已注册的子解析器。 其中Map的键值是约束条件，值是子解析器。
	 * 
	 * @return 子解析器的Map集合。
	 */
	public Map<String, XmlParser> getSubParsers() {
		return subParsers;
	}

	/**
	 * 根据约束条件返回一个匹配的子解析器。
	 * 
	 * @param constraint
	 *            约束条件。 如果无法直接通过约束条件找到一个匹配的子解析器，那么此方法将尝试返回WILDCARD相关的子解析器。
	 */
	protected XmlParser findSubParser(String constraint) {
		constraint = StringUtils.defaultString(constraint, WILDCARD);
		XmlParser parser = subParsers.get(constraint);
		if (parser == null && !WILDCARD.equals(constraint)) {
			parser = subParsers.get(WILDCARD);
		}
		return parser;
	}

	public final Object parse(Node node, ParseContext context) throws Exception {
		return doParse(node, context);
	}

	protected Object doParse(Node node, ParseContext context) throws Exception {
		if (node instanceof Element) {
			return dispatchChildElements((Element) node, context);
		} else {
			return null;
		}
	}

	private List<Element> findSubElementsByPath(Element element, String[] path,
			int deepth) {
		String pathSection = path[deepth];
		List<Element> children;
		if (WILDCARD.equals(pathSection)) {
			children = DomUtils.getChildElements(element);
		} else {
			children = DomUtils.getChildrenByTagName(element, pathSection);
		}

		if (deepth < path.length - 1) {
			List<Element> result = null;
			for (Element child : children) {
				List<Element> l = findSubElementsByPath(child, path, deepth + 1);
				if (!l.isEmpty()) {
					if (result == null) {
						result = new ArrayList<Element>();
					}
					result.addAll(l);
				}
			}
			return result;
		} else {
			return children;
		}
	}

	/**
	 * 遍历所有子节点并将解析任务分派给各个已注册的子解析器，并将所有子解析器返回的解析结果以List的方式返回。
	 * 
	 * @param element
	 *            要解析的XML节点
	 * @param context
	 *            解析的上下文对象
	 * @return 解析结果
	 * @throws Exception
	 */
	protected List<?> dispatchChildElements(Element element,
			ParseContext context) throws Exception {
		List<Object> list = new ArrayList<Object>();
		for (Element childElement : DomUtils.getChildElements(element)) {
			String nodeName = childElement.getNodeName();
			if (!XmlConstants.PROPERTY.equals(nodeName)
					&& !XmlConstants.GROUP_START.equals(nodeName)
					&& !XmlConstants.GROUP_END.equals(nodeName)
					&& !XmlConstants.IMPORT.equals(nodeName)
					&& !XmlConstants.PLACE_HOLDER.equals(nodeName)
					&& !XmlConstants.PLACE_HOLDER_START.equals(nodeName)
					&& !XmlConstants.PLACE_HOLDER_END.equals(nodeName)) {
				Object value = dispatchElement(childElement, context);
				if (value != ConfigUtils.IGNORE_VALUE) {
					list.add(value);
				}
			}
		}

		for (Map.Entry<String, XmlParser> entry : subParsers.entrySet()) {
			String key = entry.getKey();
			XmlParser subParser = entry.getValue();
			if (key.indexOf(SUB_PARSER_PATH_SEPERATOR) > 0) {
				String[] path = StringUtils.split(key,
						SUB_PARSER_PATH_SEPERATOR);
				List<Element> elements = findSubElementsByPath(element, path, 0);
				if (elements != null && !elements.isEmpty()) {
					for (Element childElement : elements) {
						Object value = subParser.parse(childElement, context);
						if (value != ConfigUtils.IGNORE_VALUE) {
							list.add(value);
						}
					}
				}
			} else if (SELF.equals(key)) {
				Object value = subParser.parse(element, context);
				if (value != ConfigUtils.IGNORE_VALUE) {
				}
				list.add(value);
			}
		}
		return list;
	}

	/**
	 * 根据传入的XML节点返回子解析器的约束条件。在此类的实现方式中直接使用子节点的名称作为约束条件。
	 * 
	 * @param node
	 *            XML节点
	 * @param context
	 *            解析的上下文对象
	 * @return String 约束条件
	 * @throws Exception
	 */
	protected String getConstraint(Node node, ParseContext context)
			throws Exception {
		if (node instanceof Element) {
			return ((Element) node).getTagName();
		} else {
			return node.getNodeName();
		}
	}

	/**
	 * 将某个XML节点的解析任务分发给匹配的子解析器。
	 * 
	 * @param element
	 *            XML节点
	 * @param context
	 *            解析的上下文对象
	 * @return 解析结果
	 * @throws Exception
	 */
	protected Object dispatchElement(Element element, ParseContext context)
			throws Exception {
		String constraint = getConstraint(element, context);
		return dispatchElement(constraint, element, context);
	}

	/**
	 * 根据传入的约束条件将某个XML节点的解析任务分发给匹配的子解析器。
	 * 
	 * @param constraint
	 *            约束条件
	 * @param element
	 *            XML节点
	 * @param context
	 *            解析的上下文对象
	 * @return 解析结果
	 * @throws Exception
	 */
	protected Object dispatchElement(String constraint, Element element,
			ParseContext context) throws Exception {
		XmlParser parser = findSubParser(constraint);
		if (parser != null) {
			return parser.parse(element, context);
		} else {
			return ConfigUtils.IGNORE_VALUE;
		}
	}

	/**
	 * 解析所有的属性，并返回属性值的集合。<br>
	 * 此处所指的属性包括XML节点中的属性(Attribute)以及下列形式的子属性节点：
	 * <code><Property name="xxx">XXXX</Property></code>。
	 * 如果二者同时存在，则将以子属性节点中的定义为准。
	 * 
	 * @param element
	 *            XML节点
	 * @param context
	 *            解析的上下文对象
	 * @return 属性值的集合，其中Map集合的键为属性名，值为相应属性的值。
	 *         注意，此处的属性值可能并不是最终的属性值，它可能是表达式或配置声明对象。
	 * @throws Exception
	 */
	protected Map<String, Object> parseProperties(Element element,
			ParseContext context) throws Exception {
		Map<String, Object> properties = new HashMap<String, Object>();

		for (Element propertyElement : DomUtils.getChildrenByTagName(element,
				XmlConstants.PROPERTY)) {
			String name = propertyElement
					.getAttribute(XmlConstants.ATTRIBUTE_NAME);
			if (StringUtils.isNotEmpty(name)) {
				properties.put(name, propertyElement);
			}
		}

		NamedNodeMap attributes = element.getAttributes();
		int attributeNum = attributes.getLength();
		for (int i = 0; i < attributeNum; i++) {
			Node node = attributes.item(i);
			String property = node.getNodeName();
			if (!properties.containsKey(property)) {
				properties.put(property, node);
			}
		}

		for (Iterator<Map.Entry<String, Object>> it = properties.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = it.next();
			String property = entry.getKey();
			Node node = (Node) entry.getValue();
			Object value = parseProperty(property, node, context);
			if (value != ConfigUtils.IGNORE_VALUE) {
				entry.setValue(value);
			} else {
				it.remove();
			}
		}
		return properties;
	}

	/**
	 * 解析给定的属性节点。
	 * 
	 * @param property
	 *            属性名
	 * @param node
	 *            XML节点
	 * @param context
	 *            解析的上下文对象
	 * @return 解析得到的属性值
	 * @throws Exception
	 */
	protected Object parseProperty(String property, Node node,
			ParseContext context) throws Exception {
		Object value;
		XmlParser propertyParser = findPropertyParser(property);
		if (propertyParser != null) {
			value = propertyParser.parse(node, context);
		} else {
			value = ConfigUtils.IGNORE_VALUE;
		}
		return value;
	}

}
