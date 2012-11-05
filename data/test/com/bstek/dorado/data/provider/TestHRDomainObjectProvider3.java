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

package com.bstek.dorado.data.provider;

import java.io.IOException;

import com.bstek.dorado.data.model.TestDataHolder;
import com.bstek.dorado.data.provider.Page;

public class TestHRDomainObjectProvider3 {

	public void getResult(Page<?> page) throws IOException {
		TestDataHolder.getDomainTestData3(page);
	}
}
