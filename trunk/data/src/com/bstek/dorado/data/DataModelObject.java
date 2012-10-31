﻿/*
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
