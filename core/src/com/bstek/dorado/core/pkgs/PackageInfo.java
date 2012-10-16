package com.bstek.dorado.core.pkgs;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-22
 */
public class PackageInfo {
	private String name;
	private String version;
	private Dependence[] depends;
	private PackageConfigurer configurer;
	private PackageListener listener;
	private String propertiesLocations;
	private String contextLocations;
	private String servletContextLocations;
	private Object userData;

	public PackageInfo(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Dependence[] getDepends() {
		return depends;
	}

	public void setDepends(Dependence[] depends) {
		this.depends = depends;
	}

	public PackageConfigurer getConfigurer() {
		return configurer;
	}

	public void setConfigurer(PackageConfigurer configurer) {
		this.configurer = configurer;
	}

	public PackageListener getListener() {
		return listener;
	}

	public void setListener(PackageListener listener) {
		this.listener = listener;
	}

	public String getPropertiesLocations() {
		return propertiesLocations;
	}

	public void setPropertiesLocations(String propertiesLocations) {
		this.propertiesLocations = propertiesLocations;
	}

	public String getContextLocations() {
		return contextLocations;
	}

	public void setContextLocations(String contextLocations) {
		this.contextLocations = contextLocations;
	}

	public String getServletContextLocations() {
		return servletContextLocations;
	}

	public void setServletContextLocations(String servletContextLocations) {
		this.servletContextLocations = servletContextLocations;
	}

	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}
}
