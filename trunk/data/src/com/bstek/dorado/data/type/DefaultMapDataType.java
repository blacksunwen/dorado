/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

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