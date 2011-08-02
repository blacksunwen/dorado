package com.bstek.dorado.console.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bstek.dorado.config.Parser;
import com.bstek.dorado.data.config.xml.GlobalDataTypeParser;
import com.bstek.dorado.data.provider.manager.DataProviderTypeRegisterInfo;
import com.bstek.dorado.data.provider.manager.DataProviderTypeRegistry;

public class ModelTree {
	
	public Collection<Node> getAllTopNodes() throws Exception {
		List<Node> nodes = new ArrayList<Node>();
		ParserContext ctx = new ParserContext();
		
		Node datatypeNode = digDataType(ctx);
		nodes.add(datatypeNode);
		
		//处理全局DataProvider节点
		Node providerNode = digDataProvider(ctx);
		nodes.add(providerNode);
		
		Node resolverNode = digDataResolver(ctx);
		nodes.add(resolverNode);
		
		return nodes;
	}

	private Node digDataProvider(ParserContext ctx) throws Exception {
		ctx.resetDoneParsers();
		
		Node providerNode = new Node(null, "DataProvider");
		DataProviderTypeRegistry registry = (DataProviderTypeRegistry)
			ctx.getDoradoContext().getServiceBean("dataProviderTypeRegistry");
		Collection<DataProviderTypeRegisterInfo> infos = registry.getTypes();
		
		for (DataProviderTypeRegisterInfo info: infos) {
			String type = info.getType();
			Parser parser = info.getParser();
			Node node = new Node(parser, type);
			node.setType(type);
			
			dig(node, ctx);
			
			providerNode.getChildren().add(node);
		}
		
		return providerNode;
	}

	private Node digDataResolver(ParserContext ctx) throws Exception {
		//处理全局DataResolver节点
		Parser resolverParser = (Parser)
			ctx.getDoradoContext().getServiceBean("globalDataResolverParser");
		Node resolverNode = new Node(resolverParser, "DataResolver");
		
		//对全局DataResolver节点挖掘
		ctx.resetDoneParsers();
		dig(resolverNode, ctx);
		return resolverNode;
	}

	private Node digDataType(ParserContext ctx) throws Exception {
		//处理全局DataType节点
		GlobalDataTypeParser datatypeParser = (GlobalDataTypeParser)
			ctx.getDoradoContext().getServiceBean("globalDataTypeParser");
		Node datatypeNode = new Node(datatypeParser, "DataType");
		
		//对全局DataType节点挖掘
		ctx.resetDoneParsers();
		dig(datatypeNode, ctx);
		return datatypeNode;
	}
	
	private void dig(Node node, ParserContext ctx) throws Exception {
		ctx.properties(node);
		ctx.children(node);
	}
}
