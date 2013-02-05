package com.bstek.dorado.console.utils;

/**
 * 服务执行日志辅助类
 * 
 * @author Alex Tong (mailto:alex.tong@bstek.com)
 * @since 2012-12-27
 */

public class ExecuteLogUtils {
	/**
	 * 格式化开始日志
	 * 
	 * @param type
	 *            服务类型
	 * @param serviceName
	 *            服务名称
	 * @param message
	 *            日志消息
	 * @return
	 */
	public static String start(String type, String serviceName, String message) {
		return String.format(" Executing %s : %s , %s. [Start]", type,
				serviceName, message);
	}

	/**
	 * 格式化结束日志
	 * 
	 * @param type
	 *            服务类型
	 * @param serviceName
	 *            服务名称
	 * @param message
	 *            日志消息
	 * @return
	 */

	public static String end(String type, String serviceName, String message) {
		return String.format(" Successfully completed %s : %s , %s. [End]",
				type, serviceName, message);
	}
}
