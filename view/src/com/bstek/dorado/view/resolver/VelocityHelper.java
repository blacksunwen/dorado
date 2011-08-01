/**
 * 
 */
package com.bstek.dorado.view.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolManager;

import com.bstek.dorado.view.View;
import com.bstek.dorado.web.WebConfigure;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-14
 */
public class VelocityHelper {
	private ToolManager toolManager;

	public VelocityHelper(ToolManager toolManager) {
		this.toolManager = toolManager;
	}

	public Context createContext(View view, HttpServletRequest request,
			HttpServletResponse response) {
		Context context = toolManager.createContext();
		context.put("view", view);
		context.put("request", request);
		context.put("response", response);
		context.put("context", com.bstek.dorado.core.Context.getCurrent());
		context.put("configure", WebConfigure.getStore());
		return context;
	}

	public VelocityEngine getVelocityEngine() {
		return toolManager.getVelocityEngine();
	}

}
