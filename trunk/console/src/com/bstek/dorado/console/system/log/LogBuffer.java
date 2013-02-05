package com.bstek.dorado.console.system.log;

import java.util.Collections;
import java.util.EventListener;
import java.util.List;
import java.util.Vector;

/**
 * @author Alex tong (mailto:alex.tong@bstek.com)
 * @since 2012-11-22
 */
class LogBuffer implements EventListener {
	public static final int MAX_LOG_COUNT = 1000;

	private List<LogLine> lines = new Vector<LogLine>(MAX_LOG_COUNT);

	/**
	 * 推送一行日志
	 * 
	 * @param event
	 */
	synchronized public void onPush(Event event) {
		if (lines.size() > MAX_LOG_COUNT - 1) {
			int sz = lines.size(), gap = sz - MAX_LOG_COUNT - 1;
			for (int i = 0; i < gap; i++) {
				lines.remove(0);
			}
		}
		LogLine line = event.getObject();
		lines.add(line);
	}

	/**
	 * 判断池是否为空
	 * 
	 * @return
	 */
	synchronized public boolean isEmpty() {
		return lines.isEmpty();
	}

	/**
	 * 获取已有日志 （获取完将初始化日志池）
	 * 
	 * @return
	 */
	synchronized public List<LogLine> getLastLines() {
		if (lines.isEmpty()) {
			return null;
		} else {
			List<LogLine> last = Collections.unmodifiableList(lines);
			lines = new Vector<LogLine>(MAX_LOG_COUNT);
			return last;
		}
	}

}
