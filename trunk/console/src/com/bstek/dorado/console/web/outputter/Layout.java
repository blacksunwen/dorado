package com.bstek.dorado.console.web.outputter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.bstek.dorado.view.registry.LayoutTypeRegisterInfo;
import com.bstek.dorado.view.registry.LayoutTypeRegistry;
import com.bstek.dorado.web.DoradoContext;

public class Layout extends Category {
	private final static String CATEGORY_NAME = "Layout";

	public Layout() {
		super(CATEGORY_NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Node> initNodes() {
		LayoutTypeRegistry registry = (LayoutTypeRegistry) DoradoContext
				.getAttachedWebApplicationContext().getBean(
						"dorado.layoutTypeRegistry");
		Collection<LayoutTypeRegisterInfo> registerInfos = registry
				.getRegisterInfos();
		Iterator<LayoutTypeRegisterInfo> iterator = registerInfos.iterator();
		List<Node> nodes = new ArrayList<Node>();
		Node node;
		while (iterator.hasNext()) {
			LayoutTypeRegisterInfo registerInfo = (LayoutTypeRegisterInfo) iterator
					.next();
			node = new Node();
			node.setName(registerInfo.getType());
			node.setBeanName(registerInfo.getClassType().getName());
			node.initProperties();
			nodes.add(node);
		}
		return nodes;
	}

}
