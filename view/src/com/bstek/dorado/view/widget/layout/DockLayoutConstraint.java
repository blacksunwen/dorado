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
package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.IdeProperty;

/**
 * Border型布局的布局条件。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 5, 2008
 */
public class DockLayoutConstraint extends LayoutConstraintSupport {
	private DockMode type = DockMode.center;

	/**
	 * 设置该布局条件代表的区域。
	 * 
	 * @param region
	 *            区域
	 */
	public void setType(DockMode type) {
		this.type = type;
	}

	/**
	 * 返回该布局条件代表的区域。
	 */
	@IdeProperty(highlight = 1)
	public DockMode getType() {
		return type;
	}
}
