package com.bstek.dorado.jdbc.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.jdbc.DbTableTrigger;
import com.bstek.dorado.jdbc.sql.CurdSqlGenerator;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractTable extends AbstractDbElement implements DbTable {

	private Map<String,AbstractColumn> columnMap = new LinkedHashMap<String,AbstractColumn>();
	private DbTableTrigger trigger;
	private CurdSqlGenerator sqlGenerator;
	
	public List<AbstractColumn> getAllColumns() {
		return new ArrayList<AbstractColumn>(columnMap.values());
	}
	
	public AbstractColumn getColumn(String name) {
		AbstractColumn c = columnMap.get(name);
		Assert.notNull(c, "No column named [" + name + "] in table [" + this.getName() + "]");
		return c;
	}
	
	public void addColumn(AbstractColumn column) {
		String columnName = column.getName();
		Assert.notEmpty(columnName, "columnName must not be empty in table [" + this.getName() + "]");
		
		if (columnMap.containsKey(columnName)) {
			throw new IllegalArgumentException("Duplicate column named [" + columnName + "] in table [" + this.getName() + "]");
		}
		columnMap.put(columnName, column);
	}

	@XmlProperty(parser="spring:dorado.jdbc.triggerParser")
	public DbTableTrigger getTrigger() {
		return trigger;
	}
	
	public void setTrigger(DbTableTrigger trigger) {
		this.trigger = trigger;
	}
	
	public CurdSqlGenerator getCurdSqlGenerator() {
		if(sqlGenerator == null) {
			sqlGenerator = getDefaultSQLGenerator();
		}
		
		return sqlGenerator;
	}
	
	public void setCurdSqlGenerator(CurdSqlGenerator sqlGenerator) {
		this.sqlGenerator = sqlGenerator;
	}
	
	protected CurdSqlGenerator getDefaultSQLGenerator() {
		String beanName = getDefaultSQLGeneratorServiceName();
		try {
			return (CurdSqlGenerator)BeanFactoryUtils.getBean(beanName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected abstract String getDefaultSQLGeneratorServiceName();
}
