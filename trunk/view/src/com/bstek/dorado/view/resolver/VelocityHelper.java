package com.bstek.dorado.view.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import com.bstek.dorado.view.View;
import com.bstek.dorado.web.WebConfigure;

public abstract class VelocityHelper {
	protected abstract Context createContext() throws Exception;

	public abstract VelocityEngine getVelocityEngine() throws Exception;

	public Context getContext(View view, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Context context = createContext();
		initContext(context, view, request, response);
		return context;
	}

	protected void initContext(Context context, View view,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		context.put("view", view);
		context.put("request", request);
		context.put("response", response);
		context.put("context", com.bstek.dorado.core.Context.getCurrent());
		context.put("configure", WebConfigure.getStore());
	}
}