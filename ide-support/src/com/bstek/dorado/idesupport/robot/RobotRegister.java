/**
 * 
 */
package com.bstek.dorado.idesupport.robot;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-9-23
 */
public class RobotRegister implements InitializingBean {
	private RobotRegistry robotRegistry;

	private String name;
	private String label;
	private String viewObject;
	private Object robot;

	public void setRobotRegistry(RobotRegistry robotRegistry) {
		this.robotRegistry = robotRegistry;
	}

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

	public void afterPropertiesSet() throws Exception {
		Assert.hasText(name);
		Assert.hasText(viewObject);
		Assert.notNull(robot);

		Map<String, RobotInfo> robotMap = robotRegistry.getRobotMap();
		if (robotMap.containsKey(name)) {
			throw new IllegalArgumentException("Robot \"" + name
					+ "\" already exists.");
		}

		RobotInfo robotInfo = new RobotInfo();
		robotInfo.setName(name);
		robotInfo.setLabel(label);
		robotInfo.setViewObject(viewObject);
		robotInfo.setRobot(robot);
		robotMap.put(name, robotInfo);
	}
}
