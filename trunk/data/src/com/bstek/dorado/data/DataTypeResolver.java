package com.bstek.dorado.data;

import com.bstek.dorado.data.type.DataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-16
 */
public interface DataTypeResolver {

	DataType getDataType(String dataTypeName) throws Exception;
}
