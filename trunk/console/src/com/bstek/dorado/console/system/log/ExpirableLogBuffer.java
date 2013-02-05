package com.bstek.dorado.console.system.log;

import java.util.List;
/**
 * @author Alex tong (mailto:alex.tong@bstek.com)
 * @since 2012-11-22
 */
public class ExpirableLogBuffer extends LogBuffer {

	private long maxIdle = 30*60 * 1000; //最大的空闲时间 默认设置为30分钟
	private long lastAccess;
	
	public ExpirableLogBuffer() {
		super();
		this.touch();
	}
	
	public boolean isExpired() {
		return (System.currentTimeMillis() - lastAccess) > maxIdle;
	}
	
	protected void touch() {
		lastAccess = System.currentTimeMillis();
	}

	@Override
	synchronized
	public boolean isEmpty() {
		try {
			return super.isEmpty();
		} finally {
			this.touch();
		}
	}
	
	@Override
	synchronized
	public List<LogLine> getLastLines() {
		try {
			return super.getLastLines();
		} finally {
			this.touch();
		}
	}
	
}
