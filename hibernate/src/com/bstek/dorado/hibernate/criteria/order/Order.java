package com.bstek.dorado.hibernate.criteria.order;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;

@XmlNode
public class Order {
	private boolean available = true;
	private String propertyName;
	private boolean ignoreCase;
	private Direction direction;

	public static enum Direction {
		asc, desc
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction d) {
		this.direction = d;
	}

	@ClientProperty(escapeValue="true")
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}
}