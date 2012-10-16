package com.bstek.dorado.web.resolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-10-17
 */
public class JVMCacheBusterGenerator implements CacheBusterGenerator {
	private static final String TIMESTAMP = String.valueOf(System
			.currentTimeMillis());

	public String getCacheBuster(String param) throws Exception {
		return TIMESTAMP;
	}

}
