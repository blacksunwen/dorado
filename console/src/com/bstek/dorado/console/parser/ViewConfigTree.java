package com.bstek.dorado.console.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bstek.dorado.config.Parser;
import com.bstek.dorado.view.ViewParser;
import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegistry;

public class ViewConfigTree {

	public Collection<Node> getAllTopNodes() throws Exception {
		List<Node> nodes = new ArrayList<Node>();
		ParserContext ctx = new ParserContext();
		
		Node modelNodel = digModel(ctx);
		nodes.add(modelNodel);
		
		Node viewNode = digView(ctx);
		nodes.add(viewNode);
		
		return nodes;
	}
	
	private Node digModel(ParserContext ctx) throws Exception {
		Node modelNode = new Node(null, "Model");

		ModelTree modelTree = new ModelTree();
		Collection<Node> nodes = modelTree.getAllTopNodes();
		for (Node node: nodes) {
			modelNode.getChildren().add(node);
		}
		
		return modelNode;
	}
	
	private Node digView(ParserContext ctx) throws Exception {
		ViewParser viewParser = (ViewParser) ctx.getDoradoContext().getServiceBean("viewParser");
		Node viewNode = new Node(viewParser, "View");
		ctx.properties(viewNode);
		
		ComponentTypeRegistry registry = (ComponentTypeRegistry)
			ctx.getDoradoContext().getServiceBean("componentTypeRegistry");
		Collection<ComponentTypeRegisterInfo> infos = registry.getRegisterInfos();
		for (ComponentTypeRegisterInfo info: infos) {
			Parser parser = info.getParser();
			String name = info.getName();
			
			Node node = new Node(parser, name);
			node.setType(info.getClassType().getName());
			
			dig(node, ctx);
			viewNode.getChildren().add(node);
		}
		
		return viewNode;
	}
	
	private void dig(Node node, ParserContext ctx) throws Exception {
		ctx.properties(node);
		ctx.children(node);
	}
}
