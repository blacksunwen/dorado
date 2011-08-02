package com.bstek.dorado.idesupport.model;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-18
 */
public class ClientEvent {
	private String name;
	private String[] parameters;
	private String reserve;
	private Object userData;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getParameters() {
		return parameters;
	}

	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}
}
