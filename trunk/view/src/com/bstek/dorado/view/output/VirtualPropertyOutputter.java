package com.bstek.dorado.view.output;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-11
 */
public interface VirtualPropertyOutputter {

	/**
	 * @param object
	 * @param property
	 * @param context
	 * @throws Exception
	 */
	void output(Object object, String property, OutputContext context)
			throws Exception;
}
