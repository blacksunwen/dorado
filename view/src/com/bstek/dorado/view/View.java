/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container;

@XmlNode(definitionType = "com.bstek.dorado.view.config.definition.ViewDefinition", parser = "spring:dorado.viewParser", implTypes = "com.bstek.dorado.view.*")
@ClientEvents({ @ClientEvent(name = "onDataLoaded"),
		@ClientEvent(name = "onComponentRegistered"),
		@ClientEvent(name = "onComponentUnregistered") })
public abstract class View extends Container {
	private ViewConfig viewConfig;
	private Map<String, Component> componentMap = new HashMap<String, Component>();
	private String packages;
	private String pageTemplate;
	private String pageUri;
	private String title;
	private String javaScriptFile;
	private String styleSheetFile;
	private String skin;

	public View(ViewConfig viewConfig) {
		this.viewConfig = viewConfig;
		if (viewConfig != null) {
			viewConfig.setView(this);
		}
	}

	@Override
	@XmlProperty(ignored = true, attributeOnly = true)
	@IdeProperty(visible = false)
	public String getId() {
		return super.getId();
	}

	@XmlProperty(ignored = true)
	@IdeProperty(visible = false)
	public String getName() {
		return (viewConfig != null) ? viewConfig.getName() : null;
	}

	@Override
	public ViewConfig getViewConfig() {
		return viewConfig;
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

	public Collection<Component> getAllComponents() {
		return componentMap.values();
	}

	@ClientProperty(ignored = true)
	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	@ClientProperty(ignored = true)
	public String getPageTemplate() {
		return pageTemplate;
	}

	public void setPageTemplate(String pageTemplate) {
		this.pageTemplate = pageTemplate;
	}

	@ClientProperty(ignored = true)
	public String getPageUri() {
		return pageUri;
	}

	public void setPageUri(String pageUri) {
		this.pageUri = pageUri;
	}

	/**
	 * 返回的客户端界面的标题。
	 */
	@ClientProperty(ignored = true)
	@IdeProperty(highlight = 1)
	public String getTitle() {
		return title;
	}

	/**
	 * 设置客户端界面的标题。
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@ClientProperty(ignored = true)
	public String getJavaScriptFile() {
		return javaScriptFile;
	}

	public void setJavaScriptFile(String javaScriptFile) {
		this.javaScriptFile = javaScriptFile;
	}

	@ClientProperty(ignored = true)
	public String getStyleSheetFile() {
		return styleSheetFile;
	}

	public void setStyleSheetFile(String styleSheetFile) {
		this.styleSheetFile = styleSheetFile;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	@Override
	@XmlSubNode(nodeName = "*", parser = "spring:dorado.childComponentParser")
	@ClientProperty(ignored = true)
	public List<Component> getChildren() {
		return super.getChildren();
	}

}
