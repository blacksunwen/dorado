package com.bstek.dorado.core;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.core.io.ResourceLoader;
import com.bstek.dorado.util.Assert;

/**
 * 通用的上下文对象。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-25
 */
public class CommonContext extends SpringApplicationContext {
	private static final String RESOURCE_LOADER_PROPERTY = "core.resourceLoader";
	private static final Log logger = LogFactory.getLog(CommonContext.class);

	private static ResourceLoader resourceLoader;

	static {
		try {
			String resourceLoaderClass = Configure
					.getString(RESOURCE_LOADER_PROPERTY);
			Assert.notEmpty(resourceLoaderClass, "\""
					+ RESOURCE_LOADER_PROPERTY + "\" not configured.");

			Class<?> cl = ClassUtils.getClass(resourceLoaderClass);
			resourceLoader = (ResourceLoader) cl.newInstance();
		}
		catch (Exception e) {
			logger.error(e, e);
		}
	}

	@Override
	protected ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	/**
	 * 初始化一个上下文对象，并将其与当前线程关联。
	 * @return 当前线程中的上下文对象。
	 * @throws Exception
	 */
	public static Context init() throws Exception {
		CommonContext context = new CommonContext();
		attachToThreadLocal(context);

		// Initialize Spring ApplicationContext
		context.initApplicationContext();
		return context;
	}

	/**
	 * 销毁当前线程中关联的上下文对象，如果存在的话。
	 */
	public static void dispose() {
		dettachFromThreadLocal();
	}
}
