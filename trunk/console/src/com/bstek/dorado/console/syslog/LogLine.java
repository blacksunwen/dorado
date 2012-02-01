package com.bstek.dorado.console.syslog;

public class LogLine {

	private String level;
	private String line;
	
	public LogLine(){}
	
	public LogLine(String line, String level) {
		this.line = line;
		this.level = level;
	}
	
	public String getLevel() {
		return level;
	}

	public String getLine() {
		return line;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
}
