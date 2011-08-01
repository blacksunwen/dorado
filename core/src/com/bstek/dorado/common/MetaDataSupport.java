/**
 * 
 */
package com.bstek.dorado.common;

import java.util.Map;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-21
 */
public interface MetaDataSupport {

	void setMetaData(Map<String, Object> metaData);

	Map<String, Object> getMetaData();
}
