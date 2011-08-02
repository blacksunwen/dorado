package com.bstek.dorado.idesupport.robot;

import java.util.Properties;

import org.w3c.dom.Node;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-9
 */
public interface Robot {
	/**
	 * @param node
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	Node execute(Node node, Properties properties) throws Exception;
}
