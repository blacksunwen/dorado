package com.bstek.dorado.hibernate.criteria.order;

import org.apache.commons.lang.StringUtils;

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

	public void setDirection(String direction) {
		if (StringUtils.isNotEmpty(direction)) {
			Direction d = Direction.valueOf(direction);
			if (d != null) {
				this.direction = d;
			} else {
				throw new IllegalArgumentException("unknown direction '"
						+ direction + "'.");
			}
		}
	}

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