package com.bstek.dorado.data.config;

/**
 * 数据配置文件的管理器的事件监听器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 20, 2007
 * @see com.bstek.dorado.data.config.DataConfigManagerEvent
 */
public interface DataConfigManagerListener {
	/**
	 * 当配置文件被装载或卸载时触发的事件。
	 * 
	 * @param event
	 *            事件描述对象
	 */
	void onConfigChanged(DataConfigManagerEvent event);
}
