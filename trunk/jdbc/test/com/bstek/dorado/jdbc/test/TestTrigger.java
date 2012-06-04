package com.bstek.dorado.jdbc.test;


public class TestTrigger extends AbstractDbElement {

	private String name;
	private String position;
	private String tableName;
	private Class<? extends org.h2.api.Trigger> triggerClazz;
	
	public TestTrigger(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public TestTrigger setPosition(String position) {
		this.position = position;
		return this;
	}
	
	public TestTrigger setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public TestTrigger setClazz(Class<? extends org.h2.api.Trigger> triggerClazz) {
		this.triggerClazz = triggerClazz;
		return this;
	}
	
	@Override
	protected String toCreateSQL() {
		String sql = "CREATE TRIGGER " + name + " " + position + " ON " + tableName + 
				" FOR EACH ROW" + " CALL \"" + triggerClazz.getName() + "\"";
		return sql;
	}
	
	@Override
	protected String toDropSQL() {
		return "DROP TRIGGER " + name;
	}
}
