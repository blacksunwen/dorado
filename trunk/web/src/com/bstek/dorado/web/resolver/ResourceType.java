package com.bstek.dorado.web.resolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-23
 */
public class ResourceType {
	private String type;
	private String contentType;
	private boolean compressible;

	public ResourceType(String type, String contentType, boolean compressible) {
		this.type = type;
		this.contentType = contentType;
		this.compressible = compressible;
	}

	public String getType() {
		return type;
	}

	public String getContentType() {
		return contentType;
	}

	public boolean isCompressible() {
		return compressible;
	}
}
