package com.bstek.dorado.jdbc.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.jdbc.model.Column;

public class ColumnParser extends ObjectParser {

	public ColumnParser() {
		super();
		this.setDefaultImpl(Column.class.getName());
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		return super.doParse(node, context);
	}
	
}
