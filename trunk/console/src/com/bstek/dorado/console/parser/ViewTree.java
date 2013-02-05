package com.bstek.dorado.console.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.config.Parser;
import com.bstek.dorado.config.xml.XmlParserHelper;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.view.ViewParser;
import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegistry;
import com.bstek.dorado.view.widget.Component;

public class ViewTree {
	private static final Log logger = LogFactory.getLog(ViewTree.class);
	private List<Node> topNodes = new ArrayList<Node>();
	
	public void build() throws Exception {
		topNodes.clear();
		
		Node viewNode = createView(new ParserContext());
		topNodes.add(viewNode);
	}
	
	public Collection<Node> getTopNodes() throws Exception {
		return topNodes;
	}

	private Node createView(ParserContext parserContext) throws Exception {
		Context context = parserContext.getDoradoContext();
		XmlParserHelper xmlParserHelper = parserContext.getXmlParserHelper();
		
		ViewParser viewParser = (ViewParser) context.getServiceBean("viewParser");
		Node viewNode = new Node(viewParser, "View");
		viewNode.initProperties();

		ComponentTypeRegistry registry = (ComponentTypeRegistry) context.getServiceBean("componentTypeRegistry");
		Collection<ComponentTypeRegisterInfo> infos = registry.getRegisterInfos();
		for (ComponentTypeRegisterInfo info : infos) {
			Class<? extends Component> clazz = info.getClassType();
			try {
				Parser parser = xmlParserHelper.getXmlParser(clazz);

				String name = info.getName();
				Node node = new Node(parser, name);
				node.initProperties();
				
				parserContext.children(node);
				viewNode.getChildren().add(node);
			} catch (Exception e) {
				logger.debug(e);
			}
			
		}

		return viewNode;
	}
}
