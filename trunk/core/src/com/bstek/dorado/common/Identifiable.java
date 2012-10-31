/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.common;

/**
 * id可变对象的通用接口。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 4, 2008
 */
public interface Identifiable {
	void setId(String id);

	String getId();
}
