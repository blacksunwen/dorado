package com.bstek.dorado.console.syslog;

import java.util.List;

public class ExpirableLogBuffer extends LogBuffer {

	private long maxIdle = 30 * 1000; //最大的空闲时间
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
