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

package com.bstek.dorado.data.type.manager;

import com.bstek.dorado.data.type.DataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2014-9-3
 */
public class DataTypeTypeRegisterInfo {
	private String type;
	private Class<? extends DataType> classType;

	public DataTypeTypeRegisterInfo(String type,
			Class<? extends DataType> classType) {
		this.type = type;
		this.classType = classType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Class<? extends DataType> getClassType() {
		return classType;
	}

	public void setClassType(Class<? extends DataType> classType) {
		this.classType = classType;
	}
}
