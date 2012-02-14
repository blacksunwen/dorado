package com.bstek.dorado.jdbc.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.jdbc.sql.CurdSqlGenerator;
import com.bstek.dorado.util.Assert;

@XmlNode (
	properties = {
		@XmlProperty(
			propertyName = "autoCreateDataType",
			propertyType = "boolean"
		),
		@XmlProperty(
			propertyName = "autoCreateDataProvider",
			propertyType = "boolean"
		)
	}
)
public abstract class AbstractTable extends AbstractDbElement implements DbTable {

	private Map<String,Column> columnMap = new LinkedHashMap<String,Column>();
	private TableTrigger trigger;
	private CurdSqlGenerator sqlGenerator;
	
	public List<Column> getAllColumns() {
		return new ArrayList<Column>(columnMap.values());
	}
	
	public Column getColumn(String name) {
		Column c = columnMap.get(name);
		Assert.notNull(c, getType() + "named [" + getName() + "]" + " has not column named [" + name + "]");
		return c;
	}
	
	public void addColumn(Column column) {
		Assert.notEmpty(column.getColumnName(), "columnName must not be empty.");
		
		String key = this.getColumnKey(column);
		if (columnMap.containsKey(key)) {
			throw new IllegalArgumentException("Duplicate column named [" + key + "]");
		}
		columnMap.put(key, column);
	}
	
	protected String getColumnKey(Column column) {
		return column.getColumnName();
	}

	@XmlProperty(parser="spring:dorado.jdbc.triggerParser")
	public TableTrigger getTrigger() {
		return trigger;
	}
	
	public void setTrigger(TableTrigger trigger) {
		this.trigger = trigger;
	}
	
	public CurdSqlGenerator getCurdSqlGenerator() {
		if(sqlGenerator == null) {
			String beanName = getDefaultSQLGeneratorName();
			if (StringUtils.isNotEmpty(beanName)) {
				try {
					sqlGenerator = (CurdSqlGenerator)BeanFactoryUtils.getBean(beanName);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		return sqlGenerator;
	}
	
	public void setCurdSqlGenerator(CurdSqlGenerator sqlGenerator) {
		this.sqlGenerator = sqlGenerator;
	}
	
	protected abstract String getDefaultSQLGeneratorName();
}
