package com.bstek.dorado.console.performance.interceptor;

import java.util.Collection;
import java.util.Iterator;

import org.aopalliance.intercept.MethodInvocation;

import com.bstek.dorado.console.Logger;
import com.bstek.dorado.console.performance.PerformanceMonitor;
import com.bstek.dorado.console.utils.ExecuteLogUtils;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.resolver.AbstractDataResolverMethodInterceptor;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.util.PathUtils;

/**
 * Dorado Console Data Resolve Method 拦截器
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com) processor
 */
public class DataResolveMethodInterceptor extends
		AbstractDataResolverMethodInterceptor {
	private static final Logger logger = Logger
			.getLog(DataResolveMethodInterceptor.class);

	private String namePattern;

	public String getNamePattern() {
		return namePattern;
	}

	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object invokeResolve(MethodInvocation methodinvocation,
			DataResolver dataresolver, DataItems dataitems, Object obj)
			throws Throwable {
		if (PathUtils.match(namePattern, dataresolver.getName())) {
			return methodinvocation.proceed();
		}
		long startTime = System.currentTimeMillis();
	
		Iterator<String> iterator = dataitems.keySet().iterator();
		StringBuffer buffer = null;
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Object object = dataitems.get(key);
			if (buffer == null) {
				buffer = new StringBuffer();
			} else {
				buffer.append(",");
			}
			buffer.append(key).append(" = ");
			if (EntityUtils.isSimpleType(object.getClass())) {
				buffer.append(object);
			} else if (object instanceof Collection) {
				Collection list = (Collection) object;
				buffer.append("{").append(object.getClass().getName())
						.append(" , size=").append(list.size()).append("}");
			} else {
				buffer.append(" { ").append(object).append(" } ");
			}
		}
		logger.info(ExecuteLogUtils.start("DataResolver",
				dataresolver.getName(),
				String.format("dataitems=[ %s ],parameter={ %s }", buffer, obj)));

		Object object = methodinvocation.proceed();

		logger.info(ExecuteLogUtils.end("DataResolver", dataresolver.getName(),
				""));

		long endTime = System.currentTimeMillis();
		PerformanceMonitor.getInstance().monitoredProcess(
				dataresolver.getName(), startTime, endTime, "DataResolve");
		return object;
	}

}