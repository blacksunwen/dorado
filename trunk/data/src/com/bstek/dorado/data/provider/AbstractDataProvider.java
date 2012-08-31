package com.bstek.dorado.data.provider;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.common.Namable;
import com.bstek.dorado.core.bean.Scopable;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.data.type.DataType;

/**
 * 数据提供者的抽象实现类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 3, 2007
 */
public abstract class AbstractDataProvider implements DataProvider, Namable,
		Scopable {
	private String name;
	private String id;
	private Scope scope;
	private DataType resultDataType;
	private Object parameter;
	private Map<String, Object> metaData;

	@XmlProperty(ignored = true, attributeOnly = true)
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

	public DataType getResultDataType() {
		return resultDataType;
	}

	public void setResultDataType(DataType resultDataType) {
		this.resultDataType = resultDataType;
	}

	@XmlProperty
	@IdeProperty(editor = "pojo")
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
		if (parameter == null && this.parameter != null) {
			parameter = this.parameter;
		}
		return internalGetResult(parameter, resultDataType);
	}

	public Object getResult(Object parameter, DataType resultDataType)
			throws Exception {
		if (parameter == null && this.parameter != null) {
			parameter = this.parameter;
		}
		if (resultDataType == null) {
			resultDataType = this.resultDataType;
		}
		return internalGetResult(parameter, resultDataType);
	}

	public void getResult(Page<?> page) throws Exception {
		internalGetResult(parameter, page, resultDataType);
	}

	public void getResult(Object parameter, Page<?> page) throws Exception {
		if (parameter == null && this.parameter != null) {
			parameter = this.parameter;
		}
		internalGetResult(parameter, page, resultDataType);
	}

	public void getResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		if (parameter == null && this.parameter != null) {
			parameter = this.parameter;
		}
		if (resultDataType == null) {
			resultDataType = this.resultDataType;
		}
		internalGetResult(parameter, page, resultDataType);
	}

	@XmlProperty(composite = true)
	@ClientProperty(ignored = true)
	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
