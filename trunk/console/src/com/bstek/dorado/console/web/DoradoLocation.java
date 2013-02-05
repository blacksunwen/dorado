package com.bstek.dorado.console.web;

public class DoradoLocation {
	private String name;
	private String beanName;
	private String method;
	private Type type;
	public enum Type{  
		ExposedService,DataType,DataProvider,DataResolver,ViewConfig  
	}  
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getTypeName() {
		return type.name();
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	
	
}
