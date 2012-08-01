package com.bstek.dorado.web.resolver;

import com.bstek.dorado.core.Context;

/**
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-6-25
 */
public final class CacheBusterUtils {
	private static CacheBusterGenerator cacheBusterGenerator;

	private CacheBusterUtils() {
	}

	private static CacheBusterGenerator getCacheBusterGenerator()
			throws Exception {
		if (cacheBusterGenerator == null) {
			cacheBusterGenerator = (CacheBusterGenerator) Context.getCurrent()
					.getServiceBean("cacheBusterGenerator");
		}
		return cacheBusterGenerator;
	}

	public static String getCacheBuster(String param) throws Exception {
		return getCacheBusterGenerator().getCacheBuster(param);
	}
}
