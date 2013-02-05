package com.bstek.dorado.console.web.outputter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegistry;
import com.bstek.dorado.web.DoradoContext;

public class Component extends Category {
	private final static String CATEGORY_NAME = "Component";

	public Component() {
		super(CATEGORY_NAME);
	}

	@Override
	public List<Node> initNodes() {
		ComponentTypeRegistry registry = (ComponentTypeRegistry) DoradoContext
				.getAttachedWebApplicationContext().getBean(
						"dorado.componentTypeRegistry");
		Collection<ComponentTypeRegisterInfo> registerInfos = registry
				.getRegisterInfos();
		Iterator<ComponentTypeRegisterInfo> iterator = registerInfos.iterator();
		List<Node> nodes = new ArrayList<Node>();
		Node node;
		while (iterator.hasNext()) {
			ComponentTypeRegisterInfo registerInfo = (ComponentTypeRegisterInfo) iterator
					.next();
			node = new Node();
			node.setName(registerInfo.getName());
			node.setBeanName(registerInfo.getClassType().getName());
			node.initProperties();
			nodes.add(node);
		}
		Collections.sort(nodes, new ComparatorNode());

		return nodes;
	}

}
