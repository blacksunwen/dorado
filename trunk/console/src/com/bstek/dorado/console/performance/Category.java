package com.bstek.dorado.console.performance;


public class Category {
	private String name;
	private Process maxTimeProcess;
	private Process excludeFirstMaxProcess;
	private Process minTimeProcess;
	private double avgTime;
	private double excludeFirstAvgTime;
	private int count;
	private Process firstProcess;
//	private int cacheProcessCount;

	public Category(String name) {
		this.name = name;
//		cacheProcessCount = new Integer(
//				Configure.getString("dorado.console.performance.maxProcess"));
	}

	synchronized public void registerProcess(Process processInfo) {
		avgTime = (avgTime * count + processInfo.getSpendTime()) / (count + 1);
		count++;
		if (firstProcess == null) {
			maxTimeProcess = processInfo;
			minTimeProcess = processInfo;
			firstProcess = processInfo;
			return;
		} else {
			excludeFirstAvgTime = (excludeFirstAvgTime * (count - 2) + processInfo
					.getSpendTime()) / (count - 1);

		}
		if (excludeFirstMaxProcess==null||processInfo.getSpendTime() >= excludeFirstMaxProcess.getSpendTime()) {
			excludeFirstMaxProcess = processInfo;
		}
		if (processInfo.getSpendTime() >= maxTimeProcess.getSpendTime()) {
			maxTimeProcess = processInfo;
			return;
		}
		if (processInfo.getSpendTime() <= minTimeProcess.getSpendTime()) {
			minTimeProcess = processInfo;
		}

	}

	public Process getMaxTimeProcess() {
		return maxTimeProcess;
	}

	public Process getMinTimeProcess() {
		return minTimeProcess;
	}

	public double getAvgTime() {
		return avgTime;
	}

	public String getName() {
		return name;
	}



	public double getExcludeFirstAvgTime() {
		return excludeFirstAvgTime;
	}

	public int getCount() {
		return count;
	}

	public Process getFirstProcess() {
		return firstProcess;
	}

	public Process getExcludeFirstMaxProcess() {
		return excludeFirstMaxProcess;
	}

	public void setExcludeFirstMaxProcess(Process excludeFirstMaxProcess) {
		this.excludeFirstMaxProcess = excludeFirstMaxProcess;
	}

//	public int getCacheProcessCount() {
//		return cacheProcessCount;
//	}
//
//	public void setCacheProcessCount(int cacheProcessCount) {
//		this.cacheProcessCount = cacheProcessCount;
//	}

	
	
	
	

}
