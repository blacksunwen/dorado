package com.bstek.dorado.jdbc.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;

public interface ParseCallback {

	Object doParse(Node node, ParseContext context)throws Exception;
}
