package com.bstek.dorado.data.type;

import com.bstek.dorado.annotation.ViewAttribute;


/**
 * Bean类型的默认实现类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 11, 2008
 */
public class DefaultEntityDataType extends EntityDataTypeSupport {
	private Object userData;
	
	@ViewAttribute(editor = "any")
	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}
}
