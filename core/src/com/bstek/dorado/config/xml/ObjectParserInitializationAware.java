package com.bstek.dorado.config.xml;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-11-23
 */
public interface ObjectParserInitializationAware {
	void postObjectParserInitialized(ObjectParser objectParser)
			throws Exception;
}
