/**
 * 
 */
package com.bstek.dorado.idesupport;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-21
 */
public class RuleConfigLoader implements BeanFactoryPostProcessor {
	private RuleTemplateBuilder ruleTemplateBuilder;
	private String configLocation;

	public void setRuleTemplateBuilder(RuleTemplateBuilder ruleTemplateBuilder) {
		this.ruleTemplateBuilder = ruleTemplateBuilder;
	}

	/**
	 * 设置要装载的资源包配置文件的路径。
	 */
	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0)
			throws BeansException {
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
	}
}
