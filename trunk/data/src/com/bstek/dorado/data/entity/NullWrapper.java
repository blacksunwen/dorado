package com.bstek.dorado.data.entity;

import com.bstek.dorado.data.type.DataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-10-4
 */
public class NullWrapper {
	private DataType dataType;

	public NullWrapper(DataType dataType) {
		this.dataType = dataType;
	}

	public DataType getDataType() {
		return dataType;
	}
}
