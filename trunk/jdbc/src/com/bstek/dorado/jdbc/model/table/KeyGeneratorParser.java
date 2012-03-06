package com.bstek.dorado.jdbc.model.table;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.jdbc.KeyGeneratorManager;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class KeyGeneratorParser extends PropertyParser {

	private KeyGeneratorManager keyGeneratorManager;
	
	public KeyGeneratorManager getKeyGeneratorManager() {
		return keyGeneratorManager;
	}

	public void setKeyGeneratorManager(KeyGeneratorManager keyGeneratorManager) {
		this.keyGeneratorManager = keyGeneratorManager;
	}


	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String name = (String)super.doParse(node, context);
		if (StringUtils.isNotEmpty(name)) {
			KeyGenerator<Object> kg = keyGeneratorManager.get(name);
			return kg;
		} else {
			return null;
		}
	}
}
