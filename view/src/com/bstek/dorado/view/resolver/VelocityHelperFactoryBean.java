/**
 * 
 */
package com.bstek.dorado.view.resolver;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.config.PropertiesFactoryConfiguration;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-14
 */
public class VelocityHelperFactoryBean implements
		FactoryBean<VelocityHelper> {
	private Properties velocityProperties;
	private Properties velocityToolProperties;
	private List<VelocityContextInitializer> contextInitializer;

	public Properties getVelocityProperties() {
		return velocityProperties;
	}

	public void setVelocityProperties(Properties velocityProperties) {
		this.velocityProperties = velocityProperties;
	}

	public Properties getVelocityToolProperties() {
		return velocityToolProperties;
	}

	public void setVelocityToolProperties(Properties velocityToolProperties) {
		this.velocityToolProperties = velocityToolProperties;
	}

	public List<VelocityContextInitializer> getContextInitializer() {
		return contextInitializer;
	}

	public void setContextInitializer(
			List<VelocityContextInitializer> contextInitializer) {
		this.contextInitializer = contextInitializer;
	}

	public Class<?> getObjectType() {
		return VelocityHelper.class;
	}

	public boolean isSingleton() {
		return true;
	}

	protected VelocityEngine getVelocityEngine() throws Exception {
		VelocityEngine velocityEngine = new VelocityEngine();
		if (velocityProperties != null) {
			velocityEngine.init(velocityProperties);
		}
		return velocityEngine;
	}

	@SuppressWarnings("rawtypes")
	protected ToolManager getToolManager() throws Exception {
		ToolManager toolManager = new ToolManager();
		if (velocityToolProperties != null) {
			PropertiesFactoryConfiguration config = new PropertiesFactoryConfiguration();
			for (Map.Entry entry : velocityToolProperties.entrySet()) {
				config.setProperty((String) entry.getKey(), entry.getValue());
			}
			toolManager.configure(config);
		}
		toolManager.setVelocityEngine(getVelocityEngine());
		return toolManager;
	}

	public VelocityHelper getObject() throws Exception {
		return new VelocityHelper(getToolManager());
	}
}
