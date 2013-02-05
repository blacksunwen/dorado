package com.bstek.dorado.console.performance.interceptor;

import java.util.Collection;

import org.aopalliance.intercept.MethodInvocation;

import com.bstek.dorado.console.Logger;
import com.bstek.dorado.console.performance.PerformanceMonitor;
import com.bstek.dorado.console.utils.ExecuteLogUtils;
import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.data.provider.AbstractDataProviderGetResultMethodInterceptor;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.util.PathUtils;

public class DataProviderGetResultMethodInterceptor extends
		AbstractDataProviderGetResultMethodInterceptor {
	private static final Logger logger = Logger
			.getLog(DataProviderGetResultMethodInterceptor.class);
	private String namePattern;

	public String getNamePattern() {
		return namePattern;
	}

	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object invokeGetResult(MethodInvocation methodinvocation,
			DataProvider dataprovider, Object obj, DataType datatype)
			throws Throwable {
		
		if (PathUtils.match(namePattern, dataprovider.getName())) {
			return methodinvocation.proceed();
		}
		long startTime = System.currentTimeMillis();
		Object parameter = null;
		for (Object arg : methodinvocation.getArguments()) {
			if (arg instanceof Record) {
				parameter = (Record) arg;
				break;
			}
		}
		logger.info(ExecuteLogUtils.start("DataProvider GetResult",
				dataprovider.getName(),
				String.format("parameter={ %s }", parameter)));
		Object object = methodinvocation.proceed();
		Object result = object;
		if (result instanceof Expression) {
			result = String.format("{%s}", Expression.class.getName());
		} else if (result instanceof Collection) {
			result = String.format("{%s , size = %s}",
					Collection.class.getName(), ((Collection) result).size());
		}
		logger.info(ExecuteLogUtils.end("DataProvider GetResult",
				dataprovider.getName(), String.format("result= %s ", result)));

		long endTime = System.currentTimeMillis();
		PerformanceMonitor.getInstance().monitoredProcess(
				dataprovider.getName(), startTime, endTime,
				"DataProviderGetResult");
		return object;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object invokeGetPagingResult(MethodInvocation methodinvocation,
			DataProvider dataprovider, Object obj, Page page, DataType datatype)
			throws Throwable {
		if (PathUtils.match(namePattern, dataprovider.getName())) {
			return methodinvocation.proceed();
		}
		long startTime = System.currentTimeMillis();
		Object parameter = null;
		for (Object arg : methodinvocation.getArguments()) {
			if (arg instanceof Record) {
				parameter = (Record) arg;
			}
		}
		logger.info(ExecuteLogUtils.start("DataProvider GetPagingResult",
				dataprovider.getName(), String.format(
						"parameter= %s ,page={ pageNo = %s , pageSize = %s}",
						parameter, page.getPageNo(), page.getPageSize())));

		Object object = methodinvocation.proceed();
		logger.info(ExecuteLogUtils.end(
				"DataProvider GetPagingResult",
				dataprovider.getName(),
				String.format("{Entities Size= %s ,pageCount= %s}",
						page.getEntityCount(), page.getPageCount())));

		long endTime = System.currentTimeMillis();
		PerformanceMonitor.getInstance().monitoredProcess(
				dataprovider.getName(), startTime, endTime,
				"DataProviderGetResult");
		return object;
	}

}
