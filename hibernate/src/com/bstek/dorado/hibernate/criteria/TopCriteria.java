package com.bstek.dorado.hibernate.criteria;

import com.bstek.dorado.annotation.XmlNode;

/**
 * 标准、尺度
 * 
 * @author mark
 */
@XmlNode(nodeName = "Criteria")
public class TopCriteria extends BaseCriteria {

	private String entityName;
	private Class<Object> entityClazz;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Class<Object> getEntityClazz() {
		return entityClazz;
	}

	public void setEntityClazz(Class<Object> entityClazz) {
		this.entityClazz = entityClazz;
	}

}
