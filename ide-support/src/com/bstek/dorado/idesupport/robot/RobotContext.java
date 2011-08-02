package com.bstek.dorado.idesupport.robot;

import com.bstek.dorado.core.CommonContext;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.Context;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-2
 */
public class RobotContext extends CommonContext {
	private final static String CONFIG_LOCATIONS = "com/bstek/dorado/core/context.xml,"
			+ "com/bstek/dorado/config/context.xml,"
			+ "com/bstek/dorado/common/context.xml,"
			+ "com/bstek/dorado/data/context.xml";

	public static Context init() throws Exception {
		Configure.getStore()
				.set("core.contextConfigLocation", CONFIG_LOCATIONS);

		RobotContext context = new RobotContext();
		attachToThreadLocal(context);

		// Initialize Spring ApplicationContext
		context.initApplicationContext();
		return context;
	}
}
