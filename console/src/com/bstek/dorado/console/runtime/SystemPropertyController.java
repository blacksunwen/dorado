package com.bstek.dorado.console.runtime;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.console.utils.SystemUtils;
import com.bstek.dorado.view.View;

/**
 * 系统参数控制器
 * 
 * @author Alex tong (mailto:alex.tong@bstek.com)
 * @since 2012-11-21
 */
public class SystemPropertyController {

	public void onReady(View view) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = SystemUtils.getSystemProperties();

		params.putAll(SystemUtils.getMemoryInfo());
		map.put("keys", params.keySet().toArray(new String[params.size()]));
		map.put("params", params);

		view.setUserData(map);
	}

	/**
	 * 强制运行垃圾回收器
	 */
	@Expose
	public void doForceGC() {
		Runtime.getRuntime().gc();
	}
}
