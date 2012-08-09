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
