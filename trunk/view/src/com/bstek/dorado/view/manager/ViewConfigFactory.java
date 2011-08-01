package com.bstek.dorado.view.manager;


/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-28
 */
public interface ViewConfigFactory {

	/**
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	ViewConfig create(String viewName) throws Exception;
}
