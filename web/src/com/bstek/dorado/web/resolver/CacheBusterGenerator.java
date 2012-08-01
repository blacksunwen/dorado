package com.bstek.dorado.web.resolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-8-1
 */
public interface CacheBusterGenerator {

	String getCacheBuster(String param) throws Exception;
}
