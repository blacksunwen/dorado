package com.bstek.dorado.view.output;

import java.util.Map;


/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-26
 */
public class TypeAnnotationInfo {
	private Outputter outputter;
	private Map<String, Object> properties;

	public Outputter getOutputter() {
		return outputter;
	}

	public void setOutputter(Outputter outputter) {
		this.outputter = outputter;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Object getPropertyConfig(String property) {
		return (properties != null) ? properties.get(property) : null;
	}
}
