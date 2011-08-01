package com.bstek.dorado.data.provider;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.bstek.dorado.common.Namable;
import com.bstek.dorado.core.bean.Scopable;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceCorrelative;
import com.bstek.dorado.data.type.DataType;

/**
 * 数据提供者的抽象实现类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 3, 2007
 */
public abstract class AbstractDataProvider implements DataProvider, Namable,
		ResourceCorrelative, Scopable {

	private String name;
	private String id;
	private Scope scope;
	private Resource resource;
	private DataType resultDataType;
	private Object parameter;

	public String getName() {
		return name;
	}

	/**
	 * 设置DataProvider的名称。
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
	 * 设置DataProvider归属的文件资源。
	 */

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public DataType getResultDataType() {
		return resultDataType;
	}

	public void setResultDataType(DataType resultDataType) {
		this.resultDataType = resultDataType;
	}

	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

	/**
	 * 内部的获得返回给外界的数据的方法。
	 * 
	 * @param parameter
	 *            参数
	 * @param resultDataType
	 *            结果的数据类型。
	 * @return 要返回给外界的数据
	 * @throws Exception
	 */
	protected abstract Object internalGetResult(Object parameter,
			DataType resultDataType) throws Exception;

	/**
	 * 内部的获得返回给外界的数据的方法。
	 * 
	 * @param parameter
	 *            参数
	 * @param page
	 *            用于封装分页结果的对象。
	 * @param resultDataType
	 *            结果的数据类型。
	 * @throws Exception
	 * @see com.bstek.dorado.data.provider.Page
	 */
	protected abstract void internalGetResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception;

	public Object getResult() throws Exception {
		return internalGetResult(parameter, resultDataType);
	}

	public Object getResult(Object parameter) throws Exception {
		return internalGetResult(parameter, resultDataType);
	}

	public Object getResult(Object parameter, DataType resultDataType)
			throws Exception {
		return internalGetResult(parameter, resultDataType);
	}

	public void getResult(Page<?> page) throws Exception {
		internalGetResult(parameter, page, resultDataType);
	}

	public void getResult(Object parameter, Page<?> page) throws Exception {
		internalGetResult(parameter, page, resultDataType);
	}

	public void getResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		internalGetResult(parameter, page, resultDataType);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
