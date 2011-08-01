package com.bstek.dorado.data;

import java.util.Collection;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-10-22
 */
public interface JsonConvertContext {
	/**
	 * @return
	 */
	public Collection<Object> getEntityCollection();

	/**
	 * @return
	 */
	public Collection<Collection<?>> getEntityListCollection();

	/**
	 * @return
	 */
	public DataTypeResolver getDataTypeResolver();
}
