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

package com.bstek.dorado.view.el;

import java.util.Map;

import com.bstek.dorado.core.el.ContextVarsInitializer;
import com.bstek.dorado.view.config.definition.AssembledComponentExpressionObject;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-9
 */
public class ViewContextVarsInitializer implements ContextVarsInitializer {
	public void initializeContext(Map<String, Object> vars) {
		vars.put("res", new ResourceExpressionHandler());
		vars.put("acomp", new AssembledComponentExpressionObject(null));
	}

}
