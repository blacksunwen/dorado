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
package com.bstek.dorado.view.config.xml;

import com.bstek.dorado.config.ParseContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-26
 */
public class PreparseContext extends ParseContext {
	private String viewName;
	private boolean inPlaceHolderSection;

	/**
	 * @param viewName
	 *            当前正被解析的视图的名称。
	 */
	public PreparseContext(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * 返回当前正被解析的视图的名称。
	 */
	public String getViewName() {
		return viewName;
	}

	public boolean isInPlaceHolderSection() {
		return inPlaceHolderSection;
	}

	public void setInPlaceHolderSection(boolean inImportableSection) {
		this.inPlaceHolderSection = inImportableSection;
	}
}
