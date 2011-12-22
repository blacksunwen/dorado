package com.bstek.dorado.data.resolver;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.common.Namable;
import com.bstek.dorado.core.bean.Scopable;
import com.bstek.dorado.core.bean.Scope;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 29, 2009
 */
public abstract class AbstractDataResolver implements DataResolver, Namable,
		Scopable {
	private String name;
	private String id;
	private Scope scope;
	private Object parameter;

	@XmlProperty(ignored = true, attributeOnly = true)
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

	@XmlProperty(unsupported = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlProperty(ignored = true, attributeOnly = true)
	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@XmlProperty(parser = "spring:dorado.preloadDataParser")
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
