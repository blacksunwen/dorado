package com.bstek.dorado.config.xml;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 24, 2008
 */
public class StaticPropertyParser extends PropertyParser {

	@Override
	protected boolean shouldEvaluateExpression() {
		return true;
	}

}
