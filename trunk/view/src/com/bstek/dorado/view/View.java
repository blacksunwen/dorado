package com.bstek.dorado.view;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.common.Namable;
import com.bstek.dorado.core.bean.Scopable;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container;

/**
 * 视图对象。
 * <p>
 * 视图对象对象用于定义和管理视图中的各种对象。
 * </p>
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 18, 2008
 */
@Widget(name = "View", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.View", outputter = "dorado.topViewOutputter")
@XmlNode(nodeName = "View", parser = "dorado.viewParser")
@ClientEvents({ @ClientEvent(name = "onDataLoaded"),
		@ClientEvent(name = "onComponentRegistered"),
		@ClientEvent(name = "onComponentUnregistered") })
public class View extends Container implements Namable, Scopable {
	private String name;
	private ViewConfig viewConfig;
	private Scope scope = Scope.request;
	private Map<String, Component> componentMap = new HashMap<String, Component>();

	private String packages;
	private String pageTemplate;
	private String pageUri;
	private String title;

	@ViewAttribute(ignored = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ViewConfig getViewConfig() {
		return viewConfig;
	}

	public void setViewConfig(ViewConfig viewConfig) {
		this.viewConfig = viewConfig;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	/**
	 * 返回作用范围。
	 */
	public Scope getScope() {
		return scope;
	}

	public void registerComponent(Component component) {
		String id = component.getId();
		if (StringUtils.isNotEmpty(id)) {
			if (componentMap.containsKey(id)) {
				throw new IllegalArgumentException("Duplicated component ["
						+ id + "].");
			}
			componentMap.put(id, component);
		}
	}

	public void unregisterComponent(Component component) {
		String id = component.getId();
		if (StringUtils.isNotEmpty(id)) {
			componentMap.remove(id);
		}
	}

	/**
	 * 根据组件id返回相应组件的对象实例。
	 */
	public Component getComponent(String componentId) {
		return componentMap.get(componentId);
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	public String getPageTemplate() {
		return pageTemplate;
	}

	public void setPageTemplate(String pageTemplate) {
		this.pageTemplate = pageTemplate;
	}

	public String getPageUri() {
		return pageUri;
	}

	public void setPageUri(String pageUri) {
		this.pageUri = pageUri;
	}

	/**
	 * 返回的客户端界面的标题。
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置客户端界面的标题。
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
