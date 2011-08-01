package com.bstek.dorado.view.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.config.xml.XmlParseException;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.util.xml.DomUtils;
import com.bstek.dorado.view.config.xml.PreparseContext;
import com.bstek.dorado.view.config.xml.ViewConfigParserUtils;
import com.bstek.dorado.view.config.xml.ViewXmlConstants;
import com.bstek.dorado.view.manager.ViewConfigManager;
import com.bstek.dorado.view.registry.ComponentTypeRegistry;

/**
 * 用于处理Import等辅助标记的XML处理器
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-26
 */
public class XmlDocumentPreprocessor implements XmlParser {
	private ViewConfigManager viewConfigManager;
	private ComponentTypeRegistry componentTypeRegistry;

	public void setViewConfigManager(ViewConfigManager viewConfigManager) {
		this.viewConfigManager = viewConfigManager;
	}

	public void setComponentTypeRegistry(
			ComponentTypeRegistry componentTypeRegistry) {
		this.componentTypeRegistry = componentTypeRegistry;
	}

	private Element doFindElementById(Element element, String id) {
		Element result = null;
		Node child = element.getFirstChild();
		while (child != null) {
			if (child instanceof Element) {
				Element childElement = (Element) child;
				if (id.equals(childElement
						.getAttribute(ViewXmlConstants.ATTRIBUTE_ID))) {
					result = childElement;
					break;
				}
			}
			child = child.getNextSibling();
		}

		if (result == null) {
			child = element.getFirstChild();
			while (child != null) {
				if (child instanceof Element) {
					result = doFindElementById((Element) child, id);
					if (result != null)
						break;
				}
				child = child.getNextSibling();
			}
		}
		return result;
	}

	private Element findElementById(Document document, String id,
			PreparseContext context) throws Exception {
		Element element = null;
		Element documentElement = document.getDocumentElement();
		Element modelElement = ViewConfigParserUtils.findModelElement(
				documentElement, context);
		if (modelElement != null) {
			element = doFindElementById(modelElement, id);
		}
		if (element == null) {
			Element viewElement = ViewConfigParserUtils.findViewElement(
					documentElement, context);
			element = doFindElementById(viewElement, id);
		}
		return element;
	}

	@SuppressWarnings("unchecked")
	private Element getGroupStartElement(Document document, String contentId,
			PreparseContext context) throws Exception {
		Element element = null;
		Map<String, Element> cache = (Map<String, Element>) document
				.getUserData("contentStartElementCache");
		if (cache != null && cache.containsKey(contentId)) {
			element = cache.get(contentId);
			return element;
		}

		element = findElementById(document, contentId, context);
		if (cache == null) {
			cache = new HashMap<String, Element>();
			document.setUserData("contentStartElementCache", cache, null);
		}
		cache.put(contentId, element);
		return element;
	}

	private ViewConfigInfo getViewConfigModelInfo(String viewName)
			throws Exception {
		Object viewConfigFactory = viewConfigManager
				.getViewConfigFactory(viewName);
		if (viewConfigFactory instanceof ViewConfigDefinitionFactory) {
			ViewConfigInfo viewConfigInfo = ((ViewConfigDefinitionFactory) viewConfigFactory)
					.getViewConfigInfo(viewName);
			return viewConfigInfo;
		} else {
			throw new XmlParseException(
					"Can not find a according ViewDefinitionFactory for ["
							+ viewName + "].");
		}
	}

	@SuppressWarnings("unchecked")
	private void postProcessNodeReplacement(Element element) {
		Document ownerDocument = element.getOwnerDocument();
		Node child = element.getFirstChild();
		while (child != null) {
			Node nextChild = child.getNextSibling();
			if (child instanceof Element) {
				if (child.getUserData("dorado.delete") != null) {
					List<Element> replaceContent = (List<Element>) child
							.getUserData("dorado.replace");
					if (replaceContent != null) {
						for (Element el : replaceContent) {
							element.insertBefore(
									ownerDocument.importNode(el, true), child);
						}
						child.setUserData("dorado.replace", null, null);
					}
					element.removeChild(child);
					child.setUserData("dorado.delete", null, null);
				}
			}
			child = nextChild;
		}
	}

	private List<Element> getImportContent(Element importElement,
			PreparseContext context) throws Exception {
		String src = importElement.getAttribute("src");

		if (StringUtils.isEmpty(src)) {
			throw new IllegalArgumentException("Import src undefined");
		}

		int i = src.lastIndexOf('#');
		if (i < 0) {
			throw new IllegalArgumentException("[groupId/componentId] missed");
		}

		String viewName = src.substring(0, i);
		String groupId = src.substring(i + 1);
		Assert.notEmpty(viewName, "Import viewName undefined.");
		Assert.notEmpty(groupId, "Import groupId/componentId undefined.");

		ViewConfigInfo viewConfigInfo = getViewConfigModelInfo(viewName);
		if (viewConfigInfo == null) {
			throw new XmlParseException("Import view not found [" + src + "].",
					context);
		}

		Document document = (Document) viewConfigInfo.getConfigModel();
		List<Element> importContent = null;
		Element groupStartElement = getGroupStartElement(document, groupId,
				context);
		if (groupStartElement != null) {
			importContent = new ArrayList<Element>();
			String nodeName = groupStartElement.getNodeName();
			if (nodeName.equals(XmlConstants.GROUP_START)) {
				boolean groupEndFound = false;
				Node node = groupStartElement.getNextSibling();
				while (true) {
					node = node.getNextSibling();
					if (node == null)
						break;
					if (node instanceof Element) {
						Element element = (Element) node;
						nodeName = element.getNodeName();
						if (nodeName.equals(XmlConstants.GROUP_END)) {
							groupEndFound = true;
							break;
						} else if (nodeName.equals(XmlConstants.GROUP_START)) {
							throw new IllegalArgumentException(
									"Nesting <GroupStart> not supported.");
						} else {
							importContent.add(element);
						}
					}
				}
				if (!groupEndFound) {
					throw new IllegalArgumentException(
							"<GroupEnd> not found for [" + groupId + "].");
				}
			} else if (nodeName.equals(XmlConstants.GROUP_END)
					|| nodeName.equals(XmlConstants.PLACE_HOLDER)
					|| nodeName.equals(XmlConstants.PLACE_HOLDER_START)
					|| nodeName.equals(XmlConstants.PLACE_HOLDER_END)) {
				// do nothing
			} else {
				importContent.add(groupStartElement);
			}
		}

		Resource dependentResource = viewConfigInfo.getResource();
		if (dependentResource != null) {
			context.getDependentResources().add(dependentResource);
		}
		return importContent;
	}

	private boolean processImports(List<Element> elements,
			PreparseContext preparseContext) throws Exception {
		boolean changed = false;
		for (Element childElement : elements.toArray(new Element[0])) {
			String nodeName = childElement.getNodeName();
			if (XmlConstants.IMPORT.equals(nodeName)) {
				List<Element> importContent = getImportContent(childElement,
						preparseContext);
				if (importContent != null) {
					childElement.setUserData("dorado.replace", importContent,
							null);
				}
				childElement.setUserData("dorado.delete", importContent, null);
				changed = true;
			}
		}
		return changed;
	}

	private void processImports(Element element, PreparseContext context)
			throws Exception {
		if (processImports(DomUtils.getChildElements(element), context)) {
			postProcessNodeReplacement(element);
		}
		for (Element childElement : DomUtils.getChildElements(element)) {
			processImports(childElement, context);
		}
	}

	private List<Element> getPlaceHolderContent(Element placeHolderElement,
			TemplateContext tempalteContext) throws Exception {
		String contentId = placeHolderElement
				.getAttribute(XmlConstants.ATTRIBUTE_ID);
		List<Element> concreteContent = null;
		Document document = tempalteContext.getSourceDocument();

		Element groupStartElement = getGroupStartElement(document, contentId,
				tempalteContext);
		if (groupStartElement != null) {
			concreteContent = new ArrayList<Element>();
			String nodeName = groupStartElement.getNodeName();
			if (nodeName.equals(XmlConstants.GROUP_START)) {
				boolean groupEndFound = false;
				Node node = groupStartElement.getNextSibling();
				while (true) {
					node = node.getNextSibling();
					if (node == null) {
						break;
					}
					if (node instanceof Element) {
						Element element = (Element) node;
						nodeName = element.getNodeName();
						if (nodeName.equals(XmlConstants.GROUP_END)) {
							groupEndFound = true;
							break;
						} else if (nodeName.equals(XmlConstants.GROUP_START)) {
							throw new IllegalArgumentException(
									"Nesting <GroupStart> not supported.");
						} else {
							concreteContent.add(element);
						}
					}
				}
				if (!groupEndFound) {
					throw new IllegalArgumentException(
							"<GroupEnd> not found for [" + contentId + "].");
				}
			} else if (nodeName.equals(XmlConstants.GROUP_END)) {
				// do nothing
			} else {
				concreteContent.add(groupStartElement);
			}
		}
		return concreteContent;
	}

	private boolean processPlaceHolders(List<Element> elements,
			TemplateContext templateContext) throws Exception {
		boolean changed = false;
		List<Element> defaultContent = null;
		Element placeHolderStartElement = null;
		for (Element childElement : elements.toArray(new Element[0])) {
			String nodeName = childElement.getNodeName();
			if (XmlConstants.PLACE_HOLDER.equals(nodeName)) {
				List<Element> concreteContent = getPlaceHolderContent(
						childElement, templateContext);
				if (concreteContent != null) {
					childElement.setUserData("dorado.replace", concreteContent,
							null);
				}
				childElement
						.setUserData("dorado.delete", concreteContent, null);
				changed = true;
			} else if (XmlConstants.PLACE_HOLDER_START.equals(nodeName)) {
				if (templateContext.isInPlaceHolderSection()) {
					throw new XmlParseException(
							"Nesting <PlaceHolderStart> not supported.",
							childElement, templateContext);
				} else {
					templateContext.setInPlaceHolderSection(true);
					defaultContent = new ArrayList<Element>();
					placeHolderStartElement = childElement;
				}
				changed = true;
			} else if (XmlConstants.PLACE_HOLDER_END.equals(nodeName)) {
				if (placeHolderStartElement == null) {
					throw new XmlParseException("<PlaceHolderStart> missed.",
							childElement, templateContext);
				} else {
					List<Element> placeHolderContent = getPlaceHolderContent(
							childElement, templateContext);
					if (placeHolderContent != null) {
						placeHolderStartElement.setUserData("dorado.replace",
								placeHolderContent, null);
						for (Element el : defaultContent) {
							el.setUserData("dorado.delete", Boolean.TRUE, null);
						}
					}
					placeHolderStartElement.setUserData("dorado.delete",
							Boolean.TRUE, null);
					childElement.setUserData("dorado.delete", Boolean.TRUE,
							null);

					templateContext.setInPlaceHolderSection(false);
					placeHolderStartElement = null;
					defaultContent = null;
				}
				changed = true;
			} else {
				processPlaceHolders(childElement, templateContext);
			}
		}
		return changed;
	}

	private void processPlaceHolders(Element element,
			TemplateContext templateContext) throws Exception {
		if (processPlaceHolders(DomUtils.getChildElements(element),
				templateContext)) {
			postProcessNodeReplacement(element);
		}
	}

	private void gothroughPlaceHolders(Document templateDocument,
			TemplateContext templateContext) throws Exception {
		Element templteViewElement = ViewConfigParserUtils.findViewElement(
				templateDocument.getDocumentElement(), templateContext);

		String packages = templteViewElement
				.getAttribute(ViewXmlConstants.ATTRIBUTE_PACKAGES);
		String oldPackages = templateContext.getSourceContext().getPackages();
		if (StringUtils.isNotEmpty(packages)
				&& StringUtils.isNotEmpty(oldPackages)) {
			packages = oldPackages + ',' + packages;
		} else {
			packages = oldPackages;
		}
		if (StringUtils.isNotEmpty(packages)) {
			templteViewElement.setAttribute(
					ViewXmlConstants.ATTRIBUTE_PACKAGES, packages);
		}

		Document document = templateContext.getSourceDocument();
		Element viewElement = ViewConfigParserUtils.findViewElement(
				document.getDocumentElement(),
				templateContext.getSourceContext());

		NamedNodeMap attributes = viewElement.getAttributes();
		int len = attributes.getLength();
		for (int i = 0; i < len; i++) {
			Node item = attributes.item(i);
			String nodeName = item.getNodeName();
			templteViewElement.setAttribute(nodeName, item.getNodeValue());
		}

		for (Element element : DomUtils.getChildElements(viewElement)) {
			String nodeName = element.getNodeName();
			if (nodeName.equals(XmlConstants.GROUP_START)
					|| nodeName.equals(XmlConstants.GROUP_END)
					|| nodeName.equals(XmlConstants.IMPORT)
					|| nodeName.equals(XmlConstants.PLACE_HOLDER)
					|| nodeName.equals(XmlConstants.PLACE_HOLDER_START)
					|| nodeName.equals(XmlConstants.PLACE_HOLDER_END)) {
				continue;
			}
			if (componentTypeRegistry.getRegisterInfo(nodeName) == null) {
				templteViewElement.appendChild(templateDocument.importNode(
						element, true));
			}
		}

		processPlaceHolders(templteViewElement, templateContext);
	}

	private Map<String, Element> getArgumentElements(Document document,
			PreparseContext context) throws Exception {
		Map<String, Element> map = new HashMap<String, Element>();
		Element argumentsElement = ViewConfigParserUtils.findArgumentsElement(
				document.getDocumentElement(), context);
		if (argumentsElement != null) {
			for (Element argumentElement : DomUtils.getChildrenByTagName(
					argumentsElement, ViewXmlConstants.ARGUMENT)) {
				map.put(argumentElement
						.getAttribute(XmlConstants.ATTRIBUTE_NAME),
						argumentElement);
			}
		}
		return map;
	}

	private void mergeArguments(Document templateDocument,
			TemplateContext templateContext) throws Exception {
		Document document = templateContext.getSourceDocument();
		Map<String, Element> argumentMap = getArgumentElements(document,
				templateContext.getSourceContext());
		Map<String, Element> templateArgumentMap = getArgumentElements(
				templateDocument, templateContext);

		if (!argumentMap.isEmpty()) {
			Element templateDocumentElement = templateDocument
					.getDocumentElement();
			Element argumentsElement = ViewConfigParserUtils
					.findArgumentsElement(templateDocumentElement,
							templateContext);
			if (argumentsElement == null) {
				argumentsElement = templateDocument
						.createElement(ViewXmlConstants.ARGUMENTS);
				templateDocumentElement.insertBefore(argumentsElement,
						templateDocumentElement.getFirstChild());
			}

			for (Map.Entry<String, Element> entry : argumentMap.entrySet()) {
				String name = entry.getKey();
				if (templateArgumentMap == null
						|| !templateArgumentMap.containsKey(name)) {
					Element element = entry.getValue();
					element = (Element) element.cloneNode(true);
					argumentsElement.appendChild(templateDocument.importNode(
							element, true));
				}
			}
		}
	}

	private void mergeModels(Document templateDocument,
			TemplateContext templateContext) throws Exception {
		Element templateDocumentElement = templateDocument.getDocumentElement();
		Element templateModelElement = ViewConfigParserUtils.findModelElement(
				templateDocumentElement, templateContext);

		Document document = templateContext.getSourceDocument();
		Element modelElement = ViewConfigParserUtils.findModelElement(
				document.getDocumentElement(),
				templateContext.getSourceContext());

		if (modelElement != null) {
			List<Element> modelElements = DomUtils
					.getChildElements(modelElement);
			if (!modelElements.isEmpty()) {
				if (templateModelElement == null) {
					templateModelElement = templateDocument
							.createElement(ViewXmlConstants.MODEL);
					Element viewElement = ViewConfigParserUtils
							.findViewElement(templateDocumentElement,
									templateContext);
					templateDocumentElement.insertBefore(templateModelElement,
							viewElement);
				}

				for (Element model : modelElements) {
					templateModelElement.appendChild(templateDocument
							.importNode(model, true));
				}
			}
		}
	}

	private Document loadTemplate(String templateViewName,
			TemplateContext templateContext) throws Exception {
		ViewConfigInfo viewConfigInfo = getViewConfigModelInfo(templateViewName);
		if (viewConfigInfo == null) {
			throw new XmlParseException("Template view not found ["
					+ templateViewName + "].",
					templateContext.getSourceContext());
		}

		Document templateDocument = (Document) viewConfigInfo.getConfigModel();
		templateDocument = (Document) templateDocument.cloneNode(true);

		mergeArguments(templateDocument, templateContext);
		mergeModels(templateDocument, templateContext);
		gothroughPlaceHolders(templateDocument, templateContext);
		return templateDocument;
	}

	public Object parse(Node node, ParseContext context) throws Exception {
		PreparseContext preparseContext = (PreparseContext) context;
		Document document = (Document) node;
		Element documentElement = document.getDocumentElement();

		Element viewElement = ViewConfigParserUtils.findViewElement(
				documentElement, preparseContext);
		String packages = viewElement
				.getAttribute(ViewXmlConstants.ATTRIBUTE_PACKAGES);
		preparseContext.setPackages(packages);

		String templateSrc = documentElement
				.getAttribute(ViewXmlConstants.ATTRIBUTE_TEMPALTE);
		if (StringUtils.isNotEmpty(templateSrc)) {
			TemplateContext templateContext = new TemplateContext(templateSrc,
					preparseContext, document);
			Document templateDocument = loadTemplate(templateSrc,
					templateContext);
			document = templateDocument;
		}

		processImports(documentElement, preparseContext);
		return document;
	}

}

class TemplateContext extends PreparseContext {
	private PreparseContext sourceContext;
	private Document sourceDocument;

	public PreparseContext getSourceContext() {
		return sourceContext;
	}

	public TemplateContext(String viewName, PreparseContext sourceContext,
			Document sourceDocument) {
		super(viewName);
		this.sourceContext = sourceContext;
		this.sourceDocument = sourceDocument;
	}

	public Document getSourceDocument() {
		return sourceDocument;
	}
}
