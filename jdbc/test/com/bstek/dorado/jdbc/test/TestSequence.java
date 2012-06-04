package com.bstek.dorado.jdbc.test;


public class TestSequence extends AbstractDbElement{

	private String name;
	private int start = 1;
	private int increment = 1;
	
	public TestSequence(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public TestSequence setStart(int start) {
		this.start = start;
		return this;
	}
	
	public TestSequence setIncrement(int inc) {
		this.increment = inc;
		return this;
	}
	
	@Override
	protected String toCreateSQL() {
		String sql = "CREATE SEQUENCE " + name + " START WITH " + start + " INCREMENT BY " + increment;
		return sql;
	}
	
	@Override
	protected String toDropSQL() {
		return "DROP SEQUENCE " + name;
	}
}
