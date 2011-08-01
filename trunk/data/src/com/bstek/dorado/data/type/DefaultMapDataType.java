package com.bstek.dorado.data.type;

/**
 * Map类型的默认实现类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 11, 2008
 */
public class DefaultMapDataType extends EntityDataTypeSupport implements
		MapDataType {
	private DataType keyDataType;
	private DataType valueDataType;

	public DataType getKeyDataType() {
		return keyDataType;
	}

	public void setKeyDataType(DataType keyDataType) {
		this.keyDataType = keyDataType;
	}

	public DataType getValueDataType() {
		return valueDataType;
	}

	public void setValueDataType(DataType valueDataType) {
		this.valueDataType = valueDataType;
	}
}
