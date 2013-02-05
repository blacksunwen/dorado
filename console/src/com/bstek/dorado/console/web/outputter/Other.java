package com.bstek.dorado.console.web.outputter;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.view.DefaultView;

public class Other extends Category {

	private final static String CATEGORY_NAME = "Other";

	public Other() {
		super(CATEGORY_NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Node> initNodes() {
		List<Node> list = new ArrayList<Node>();

		Node node = new Node();
		node.setName("DefaultView");
		node.setBeanName(DefaultView.class.getName());
		node.initProperties();
		list.add(node);
       
		return list;
	}

}
