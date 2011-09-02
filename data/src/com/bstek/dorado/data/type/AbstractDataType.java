package com.bstek.dorado.data.type;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.XmlProperty;

/**
 * DataType的抽象实现类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 9, 2007
 */
public abstract class AbstractDataType implements RudeDataType {
	private String name;
	private String id;
	private Class<?> matchType;
	private Class<?> creationType;
	private String tags;
	private Map<String, Object> metaData;

	public String getName() {
		return name;
	}

	/**
	 * 设置DataType的名称。
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

	public Class<?> getMatchType() {
		return matchType;
	}

	public void setMatchType(Class<?> matchType) {
		this.matchType = matchType;
		if (creationType == null) {
			creationType = matchType;
		}
	}

	public Class<?> getCreationType() {
		return creationType;
	}

	public void setCreationType(Class<?> creationType) {
		this.creationType = creationType;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String toText(Object value) {
		return (value == null) ? null : value.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		}

		Class<?> targetType = this.getMatchType();
		if (targetType != null) {
			if (targetType.isAssignableFrom(value.getClass())) {
				return value;
			} else if (value instanceof String && targetType.isEnum()) {
				return Enum.valueOf((Class<? extends Enum>) targetType,
						(String) value);
			}
		}

		throw new DataConvertException(value.getClass(), getMatchType());
	}

	@Override
	public String toString() {
		return ObjectUtils.identityToString(this) + " [" + "name=" + getName()
				+ ", " + "matchType=" + getMatchType() + "]";
	}

	@XmlProperty(composite = true)
	@ViewAttribute(outputter = "#ignore")
	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}

}
