/**
 * 
 */
package com.bstek.dorado.idesupport.robot;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-9-23
 */
public class RobotRegistry {
	private Map<String, RobotInfo> robotMap = new HashMap<String, RobotInfo>();

	public Map<String, RobotInfo> getRobotMap() {
		return robotMap;
	}

	public Robot getRobot(String name) throws Exception {
		if (robotMap == null) {
			throw new IllegalStateException("\"robotMap\" not initialized.");
		}

		Robot robot = null;
		RobotInfo robotInfo = robotMap.get(name);
		if (robotInfo != null) {
			Object robotDef = robotInfo.getRobot();
			if (robotDef instanceof Robot) {
				robot = (Robot) robotDef;
			} else if (robotDef instanceof String) {
				DoradoContext context = DoradoContext.getCurrent();
				robot = (Robot) context.getApplicationContext().getBean(
						(String) robotDef);
			}
		}

		return robot;
	}
}
