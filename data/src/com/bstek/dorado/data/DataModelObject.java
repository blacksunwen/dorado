package com.bstek.dorado.data;

import com.bstek.dorado.common.MetaDataSupport;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-27
 */
public interface DataModelObject extends MetaDataSupport {
	String getName();

	String getId();

	void setId(String id);
}
