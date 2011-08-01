package com.bstek.dorado.core.io;

/**
 * 与资源相关的对象的通用接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 10, 2007
 */
public interface ResourceCorrelative {
	/**
	 * 设置相关的资源。
	 */
	void setResource(Resource resource);

	/**
	 * 返回相关的资源。
	 */
	Resource getResource();
}
