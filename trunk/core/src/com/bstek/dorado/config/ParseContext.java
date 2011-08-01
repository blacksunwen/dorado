package com.bstek.dorado.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bstek.dorado.core.io.Resource;

/**
 * 解析配置文件时使用的上下文对象。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 20, 2007
 * @see com.bstek.dorado.core.io.Resource
 */
public class ParseContext {
	private Resource resource;
	private Set<Resource> dependentResources = new HashSet<Resource>();
	private Map<String, Object> attributes = new HashMap<String, Object>();

	/**
	 * 返回当前解析的配置文件的资源描述对象。
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * 设置当前解析的配置文件的资源描述对象。
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * @return
	 */
	public Set<Resource> getDependentResources() {
		return dependentResources;
	}

	/**
	 * 返回与此上下文关联的一组属性。
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

}
