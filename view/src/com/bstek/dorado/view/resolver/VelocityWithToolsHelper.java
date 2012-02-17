/**
 * 
 */
package com.bstek.dorado.view.resolver;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-17
 */
public class VelocityWithToolsHelper extends VelocityHelper {
	private Object toolManager;

	public VelocityWithToolsHelper(Object toolManager) {
		this.toolManager = toolManager;
	}

	@Override
	protected Context createContext() throws Exception {
		return (Context) MethodUtils.invokeMethod(toolManager, "createContext",
				new Object[0]);
	}

	@Override
	public VelocityEngine getVelocityEngine() throws Exception {
		return (VelocityEngine) MethodUtils.invokeMethod(toolManager,
				"getVelocityEngine", new Object[0]);
	}

}
