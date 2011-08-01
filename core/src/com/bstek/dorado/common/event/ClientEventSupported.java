package com.bstek.dorado.common.event;

import java.util.List;
import java.util.Map;

/**
 * 支持客户端事件的对象的通用接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 10, 2008
 */
public interface ClientEventSupported {
	/**
	 * 添加一个事件监听器。
	 * @param eventName 事件名。
	 * @param eventListener 事件监听器。
	 */
	void addClientEventListener(String eventName, ClientEvent eventListener);

	/**
	 * 根据事件名返回所有已添加的事件监听器。
	 * @param eventName 事件名。
	 * @return 事件监听器的列表集合。
	 */
	List<ClientEvent> getClientEventListeners(String eventName);

	/**
	 * 清除所有某事件中的监听器。
	 * @param eventName 事件名。
	 */
	void clearClientEventListeners(String eventName);

	/**
	 * 返回所有已添加的事件监听器。
	 * @return 包含各种事件下所有事件监听器的Map集合。
	 */
	Map<String, List<ClientEvent>> getAllClientEventListeners();
}
