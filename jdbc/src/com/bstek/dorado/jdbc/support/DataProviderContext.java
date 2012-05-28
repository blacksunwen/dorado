package com.bstek.dorado.jdbc.support;

import java.util.Map;

import com.bstek.dorado.data.ParameterWrapper;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * {@link JdbcDataProvider}执行时的上下文
 * 
 * @author mark.li@bstek.com
 * 
 */
public class DataProviderContext extends AbstractJdbcContext {
	private Page<Record> page;
	private boolean autoFilter = false;
	private Criteria criteria;
	
	private EntityDataType dataType;
	
	public DataProviderContext() {
		this(null, null, null);
	}

	public DataProviderContext(JdbcEnviroment enviroment){
		this(enviroment, null, null);
	}
	
	public DataProviderContext(JdbcEnviroment enviroment, Object parameter) {
		this(enviroment, parameter, null);
	}
	
	public DataProviderContext(JdbcEnviroment enviroment, Object parameter, Page<Record> page) {
		super(enviroment, null);
		if (parameter instanceof ParameterWrapper) {
			ParameterWrapper pw = (ParameterWrapper)parameter;
			Map<String, Object> sysParameter = pw.getSysParameter();
			
			if (sysParameter instanceof Record) {
				Record paraRecord = (Record)sysParameter;
				this.setCriteria((Criteria)paraRecord.get("criteria"));
			}
			
			this.setParameter(pw.getParameter());
		} else {
			this.setParameter(parameter);
		}
		
		if (page == null) {
			page = new Page<Record>(0,0);
		}
		this.page = page;
	}

	public EntityDataType getDataType() {
		return dataType;
	}

	public void setDataType(EntityDataType dataType) {
		this.dataType = dataType;
	}

	public Page<Record> getPage() {
		return page;
	}

	public boolean isAutoFilter() {
		return autoFilter;
	}

	public void setAutoFilter(boolean autoFilter) {
		this.autoFilter = autoFilter;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}
	
}
