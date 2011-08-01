package com.bstek.dorado.data.resolver;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.bstek.dorado.common.Namable;
import com.bstek.dorado.core.bean.Scopable;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceCorrelative;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 29, 2009
 */
public abstract class AbstractDataResolver implements DataResolver, Namable,
		ResourceCorrelative, Scopable {
	private String name;
	private String id;
	private Scope scope;
	private Resource resource;
	private Object parameter;

	public String getName() {
		return name;
	}

	/**
	 * 设置DataResolver的名称。
	 */
	public void setName(String name) {
		this.name = name;
		if (StringUtils.isEmpty(id)) {
			id = name;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Resource getResource() {
		return resource;
	}

	/**
	 * 设置DataResolver归属的文件资源。
	 */

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

	protected abstract Object internalResolve(DataItems dataItems,
			Object parameter) throws Exception;

	public Object resolve(DataItems dataItems) throws Exception {
		return internalResolve(dataItems, parameter);
	}

	public Object resolve(DataItems dataItems, Object parameter)
			throws Exception {
		return internalResolve(dataItems, parameter);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
