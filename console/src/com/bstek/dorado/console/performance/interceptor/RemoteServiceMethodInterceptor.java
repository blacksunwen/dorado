package com.bstek.dorado.console.performance.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import com.bstek.dorado.common.proxy.PatternMethodInterceptor;
import com.bstek.dorado.common.service.ExposedService;
import com.bstek.dorado.common.service.ExposedServiceManager;
import com.bstek.dorado.console.Logger;
import com.bstek.dorado.console.performance.PerformanceMonitor;
import com.bstek.dorado.console.utils.ExecuteLogUtils;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.web.DoradoContext;

/**
 * Dorado Console RemoteService 拦截器
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * 
 */
public class RemoteServiceMethodInterceptor extends PatternMethodInterceptor {
	private static final Logger logger = Logger
			.getLog(RemoteServiceMethodInterceptor.class);

	private String namePattern;

	public String getNamePattern() {
		return namePattern;
	}

	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		String serviceName = null, parameter = null, metaData = null;
		Object object = null;
		long startTime = System.currentTimeMillis();
		for (Object arg : invocation.getArguments()) {
			if (arg instanceof ObjectNode) {
				ObjectNode objectNode = (ObjectNode) arg;
				try {
					serviceName = JsonUtils.getString(objectNode, "service");
					if (StringUtils.isNotEmpty(serviceName)) {
						ExposedServiceManager exposedServiceManager = (ExposedServiceManager) DoradoContext
								.getCurrent().getServiceBean(
										"exposedServiceManager");
						ExposedService exposedService = exposedServiceManager
								.getService(serviceName);
						if (exposedService == null) {
							throw new IllegalArgumentException(
									"Unknown ExposedService [" + serviceName
											+ "].");
						}
						JsonNode paramJson = objectNode.get("parameter"), metaJson = objectNode
								.get("sysParameter");
						parameter = paramJson == null ? "null" : paramJson
								.toString();
						metaData = metaJson == null ? "null" : metaJson
								.toString();

						break;
					}
				} catch (Exception e) {
				}

			}
		}
		if (StringUtils.isNotEmpty(serviceName)
				&& !PathUtils.match(namePattern, serviceName)) {

			logger.info(ExecuteLogUtils.start("service", serviceName, String
					.format("parameter=%s,metaData=%s", parameter, metaData)));
			object = invocation.proceed();
			logger.info(ExecuteLogUtils.end("service", serviceName, ""));

			long endTime = System.currentTimeMillis();

			PerformanceMonitor.getInstance().monitoredProcess(serviceName,
					startTime, endTime, "RemoteService");

		} else {
			object = invocation.proceed();
		}
		return object;
	}
}
