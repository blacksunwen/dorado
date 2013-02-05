package com.bstek.dorado.console.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserHelper;
import com.bstek.dorado.config.xml.XmlParserHelper.XmlParserInfo;
import com.bstek.dorado.util.clazz.ClassUtils;

public class ParserTree {

	@DataProvider
	public Collection<Node> getModelNodes() throws Exception {
		ModelTree modelTree = new ModelTree();
		modelTree.build();
		
		return modelTree.getTopNodes();
	}

	@DataProvider
	public Collection<Node> getViewConfigNodes() throws Exception {
		ViewTree viewTree = new ViewTree();
		viewTree.build();
		
		return viewTree.getTopNodes();
	}
	
	@DataProvider
	public Collection<Node> getTopNodes(String className) throws Exception {
		ParserContext parserContext = new ParserContext();
		XmlParserHelper xmlParserHelper = parserContext.getXmlParserHelper();
		Class<?> beanType = ClassUtils.forName(className);
		List<Node> topNodes = new ArrayList<Node>();
		List<XmlParserInfo> parserInfos = xmlParserHelper.getXmlParserInfos(beanType);
		for (XmlParserInfo parserInfo: parserInfos) {
			XmlParser parser = parserInfo.getParser();
			String name = parserInfo.getPath();
			Node node = new Node(parser, name);
			node.initProperties();
			parserContext.children(node);
			
			topNodes.add(node);
		}
		
		return topNodes;
	}
}
