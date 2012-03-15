package com.bstek.dorado.web.resolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-23
 */
public abstract class HttpConstants {
	public static final String CONTENT_TYPE_HTML = "text/html";
	public static final String CONTENT_TYPE_XML = "text/xml";
	public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
	public static final String CONTENT_TYPE_JAVASCRIPT = "text/javascript";
	public static final String CONTENT_TYPE_CSS = "text/css";
	public static final String CONTENT_TYPE_TEXT = "application/text";

	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	public static final String LAST_MODIFIED = "Last-Modified";

	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String GZIP = "gzip";
	public static final String COMPRESS = "compress";

	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String NO_CACHE = "no-cache, no-store";
}
