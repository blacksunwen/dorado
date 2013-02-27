package com.bstek.dorado.console.performance;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.console.performance.dao.PerformanceDao;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.web.DoradoContext;
/**
 * 性能监视器类
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * @since 2012-12-12
 * 
 */
public class PerformanceMonitor {
	private static PerformanceMonitor instance = new PerformanceMonitor();;
	private ProcessBase processBase = new ProcessBase();
	private Map<String, Process> lastProcessMap = new Hashtable<String, Process>();
	private Map<String, MonitoredTarget> monitoredTargets = new Hashtable<String, MonitoredTarget>();
	private Map<String, Category> categoryMap = new Hashtable<String, Category>();

	public static PerformanceMonitor getInstance() {
		return instance;
	}

	/**
	 * 储存性能信息
	 * 
	 * @param process
	 * @throws Exception
	 */
	public void registerProcess(Process process) throws Exception {
		String name = process.getName();
		lastProcessMap.put(name, process);
		Category category = categoryMap.get(name);
		if (category == null) {
			category = new Category(name);
		}
		category.registerProcess(process);
		categoryMap.put(name, category);
		if (monitoredTargets.get(name) != null) {
			processBase.pushProcess(process);
		}
	}

	/**
	 * 强制提交缓存中的性能数据
	 * 
	 * @throws Exception
	 */
	public void saveProcessListToDB() throws Exception {
		processBase.saveToDB();
	}

	/**
	 * 删除监控目标
	 * 
	 * @param name
	 */
	public void removeMonitoredTarget(String name) {
		monitoredTargets.remove(name);
	}

	/**
	 * 添加一个监控目标
	 * 
	 * @param monitoredTarget
	 * @return
	 */
	public boolean addMonitoredTarget(MonitoredTarget monitoredTarget) {
		boolean isSuccee = false;
		if (monitoredTargets.get(monitoredTarget.getName()) == null) {
			monitoredTargets.put(monitoredTarget.getName(), monitoredTarget);
			isSuccee = true;
		}
		return isSuccee;
	}

	/**
	 * 获得监控目标键值集合
	 * 
	 * @return
	 */
	public Map<String, MonitoredTarget> getMonitoredTargets() {
		return monitoredTargets;
	}

	/**
	 * 
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @param type
	 */
	public void monitoredProcess(String name, long startTime, long endTime,
			String type) {
		if (StringUtils.isNotEmpty(name)) {
			Process process = new Process();
			process.setName(name);
			process.setTime(startTime);
			process.setSpendTime(endTime - startTime);
			process.setType(type);
			process.setFreeMemory(Runtime.getRuntime().freeMemory());
			try {
				registerProcess(process);
			} catch (Exception e) {
		
			}
		}
	}

	/**
	 * 获得全局dorado请求(已发生)列表 </p>
	 * 
	 * @return
	 */
	public Collection<Process> getLastProcess() {
		return Collections.unmodifiableCollection(lastProcessMap.values());
	}

	/**
	 * 获得最近发生的请求键值对集合
	 * <p>
	 * 通过此方法可获得全局dorado请求(已发生)列表
	 * </p>
	 * 
	 * @return
	 */
	public Map<String, Process> getLastProcessMap() {
		return lastProcessMap;
	}

	/**
	 * 根据请求名获得Category
	 * 
	 * @param name
	 * @return
	 */
	public Category getCategory(String name) {
		return categoryMap.get(name);
	}

	public Map<String, Category> getCategoryMap() {
		return categoryMap;
	}

	private PerformanceMonitor() {

	}

}

class ProcessBase {

	private List<Process> processList;
	private int maxProcess;

	public ProcessBase() {
		maxProcess=Long.valueOf(Configure.getLong("dorado.console.performance.maxProcess", 100)).intValue();
		processList = new Vector<Process>(maxProcess);
	}

	/**
	 * 推送一条性能数据
	 * <p>
	 * 当性能数据设定数量时保存到数据库中并重置数据队列
	 * </p>
	 * 
	 * @param process
	 * @throws Exception
	 */
	public synchronized void pushProcess(Process process) throws Exception {
		if (processList.size() >= maxProcess) {
			saveToDB();
		}
		processList.add(process);
	}

	/**
	 * 强制提交性能数据
	 * 
	 * @throws Exception
	 */
	public synchronized void saveToDB() throws Exception {
		PerformanceDao dao = (PerformanceDao) DoradoContext
				.getAttachedWebApplicationContext().getBean(
						"dorado.console.performanceDao");
		dao.saveProcessList(processList);
		processList = new Vector<Process>(maxProcess);
	}
}