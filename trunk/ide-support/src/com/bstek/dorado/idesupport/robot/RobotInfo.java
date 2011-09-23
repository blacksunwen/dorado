package com.bstek.dorado.idesupport.robot;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-17
 */
public class RobotInfo {
	private String name;
	private String label;
	private String viewObject;
	private Object robot;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getViewObject() {
		return viewObject;
	}

	public void setViewObject(String viewObject) {
		this.viewObject = viewObject;
	}

	public Object getRobot() {
		return robot;
	}

	public void setRobot(Object robot) {
		this.robot = robot;
	}
}
