package com.bstek.dorado.console.performance;

import java.util.Comparator;

public class Process implements Comparator<Process> {
	private String name;
	private String type;
	private long time;
	private long spendTime;
	private long freeMemory;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(long spendTime) {
		this.spendTime = spendTime;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public int compare(Process o1, Process o2) {
		return (int) (o1.getSpendTime() - o2.getSpendTime());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Process [name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", time=");
		builder.append(time);
		builder.append(", spendTime=");
		builder.append(spendTime);
		builder.append(", freeMemory=");
		builder.append(freeMemory);
		builder.append("]");
		return builder.toString();
	}

}
