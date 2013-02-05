package com.bstek.dorado.console.performance.listener;

import com.bstek.dorado.console.Constants;
import com.bstek.dorado.console.Logger;
import com.bstek.dorado.console.performance.PerformanceMonitor;
import com.bstek.dorado.console.security.HtmlViewSecurityInterceptor;
import com.bstek.dorado.console.utils.ExecuteLogUtils;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.view.View;
import com.bstek.dorado.web.DoradoContext;

/**
 * Dorado Console 全局 拦截器
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * 
 */
public class ViewResolverListener implements
		com.bstek.dorado.view.resolver.ViewResolverListener {
	private static final Logger logger = Logger
			.getLog(HtmlViewSecurityInterceptor.class);
	private String viewNamePattern;

	public String getViewNamePattern() {
		return viewNamePattern;
	}

	public void setViewNamePattern(String viewNamePattern) {
		this.viewNamePattern = viewNamePattern;
	}

	public void beforeResolveView(String viewName) throws Exception {
		long startTime = System.currentTimeMillis();
		if (!PathUtils.match(viewNamePattern, viewName.replace('/', '.'))) {
			DoradoContext.getAttachedRequest().setAttribute(
					Constants.R_DORADO_CONSOLE_REQUEST_STARTTIME, startTime);
			logger.info(ExecuteLogUtils.start("Create Dorado View ", viewName,
					"request uri="
							+ DoradoContext.getAttachedRequest()
									.getRequestURI()));
		}
	}

	public void afterResolveView(String viewName, View view) throws Exception {
		if (!PathUtils.match(viewNamePattern, viewName.replace('/', '.'))) {
			long endTime = System.currentTimeMillis();
			long startTime = (Long) DoradoContext.getAttachedRequest()
					.getAttribute(Constants.R_DORADO_CONSOLE_REQUEST_STARTTIME);
			PerformanceMonitor.getInstance().monitoredProcess(viewName,
					startTime, endTime, "CreateDoradoView");
			logger.info(ExecuteLogUtils.end("Create Dorado View ", viewName,
					"request uri="
							+ DoradoContext.getAttachedRequest()
									.getRequestURI()));
		}
	}

}
