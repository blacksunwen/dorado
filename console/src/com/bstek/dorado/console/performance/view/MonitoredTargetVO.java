package com.bstek.dorado.console.performance.view;

import com.bstek.dorado.console.performance.MonitoredTarget;

public class MonitoredTargetVO extends MonitoredTarget {
	private long time;
	private long spendTime;
	private long freeMemory;
	private double avgTime;
	private long count;
	private long frequency;
	private long minTime;
	private long maxTime;

	public double getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(double avgTime) {
		this.avgTime = avgTime;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

	public long getMinTime() {
		return minTime;
	}

	public void setMinTime(long minTime) {
		this.minTime = minTime;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

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

}
