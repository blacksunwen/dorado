/**
 * 
 */
package com.bstek.dorado.idesupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.idesupport.initializer.RuleTemplateInitializer;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-21
 */
public class RuleConfigLoader implements InitializingBean {
	private RuleTemplateBuilder ruleTemplateBuilder;
	private String configLocation;
	private Map<String, RuleTemplateInitializer> initializerMap;
	
	
	public void setRuleTemplateBuilder(RuleTemplateBuilder ruleTemplateBuilder) {
		this.ruleTemplateBuilder = ruleTemplateBuilder;
	}

	/**
	 * 设置要装载的资源包配置文件的路径。
	 */
	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	/**
	 * 设置生成规则文件的拦截器
	 * @param initializerMap
	 */
	public void setInitializerMap(
			Map<String, RuleTemplateInitializer> initializerMap) {
		this.initializerMap = initializerMap;
	}

	public void afterPropertiesSet() throws Exception {
		if (configLocation != null) {
			List<String> configTemplateFiles = ruleTemplateBuilder
					.getConfigTemplateFiles();
			if (configTemplateFiles != null) {
				configTemplateFiles.add(configLocation);
			} else {
				configTemplateFiles = new ArrayList<String>();
				configTemplateFiles.add(configLocation);
				ruleTemplateBuilder.setConfigTemplateFiles(configTemplateFiles);
			}
		}
		
		if (initializerMap != null) {
			ruleTemplateBuilder.appendInitializerMap(initializerMap);
		}
	}
}
