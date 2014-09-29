package com.bstek.dorado.vidorsupport.output;


public abstract class AbstractField<T> implements IOutputable<OutputContext> {

	private String name;
	private T value;
	private T ignoredValue;
	
	protected AbstractField(String name, T ignoredValue) {
		super();
		this.name = name;
		this.setIgnoredValue(ignoredValue);
	}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public T getIgnoredValue() {
		return ignoredValue;
	}
	
	public void setIgnoredValue(T ignoredValue) {
		this.ignoredValue = ignoredValue;
	}

	public boolean shouldOutput() {
		if (value == null) {
			return false;
		} 
		// ignoredValue == null && value != null
		if (ignoredValue == null) {
			return true;
		} else {
			// ignoredValue != null && value != null
			return !ignoredValue.equals(value);
		}
	}

}
