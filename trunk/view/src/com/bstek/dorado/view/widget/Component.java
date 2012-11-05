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

package com.bstek.dorado.view.widget;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.ResourceInjection;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.common.Ignorable;
import com.bstek.dorado.common.MetaDataSupport;
import com.bstek.dorado.common.TagSupport;
import com.bstek.dorado.common.event.ClientEvent;
import com.bstek.dorado.common.event.ClientEventHolder;
import com.bstek.dorado.common.event.ClientEventSupported;
import com.bstek.dorado.util.proxy.BeanExtender;
import com.bstek.dorado.util.proxy.ProxyBeanUtils;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.ViewElementUtils;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.registry.AssembledComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.VirtualEventDescriptor;

/**
 * 组件的抽象类
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 19, 2008
 */
@XmlNode(
		definitionType = "com.bstek.dorado.view.config.definition.ComponentDefinition",
		parser = "spring:dorado.componentParser")
@ClientObject(prototype = "dorado.widget.Component",
		shortTypeName = "Component",
		outputter = "spring:dorado.componentOutputter",
		properties = { @ClientProperty(propertyName = "DEFINITION",
				outputter = "spring:dorado.assembledComponentDefOutputter") })
@ClientEvents({
		@com.bstek.dorado.annotation.ClientEvent(name = "onAttributeChange"),
		@com.bstek.dorado.annotation.ClientEvent(name = "onCreate"),
		@com.bstek.dorado.annotation.ClientEvent(name = "onDestroy"),
		@com.bstek.dorado.annotation.ClientEvent(name = "onReady") })
@ResourceInjection
public abstract class Component implements Ignorable, TagSupport,
		MetaDataSupport, ClientEventSupported, ViewElement {
	private Collection<ViewElement> innerElements;

	private String id;
	private ViewElement parent;
	private View view;
	private boolean ignored;
	private String tags;
	private Object userData;
	private Map<String, Object> metaData;
	private ClientEventHolder clientEventHolder = new ComponentClientEventHolder(
			this);

	public void addClientEventListener(String eventName, ClientEvent clientEvent) {
		clientEventHolder.addClientEventListener(eventName, clientEvent);
	}

	public List<ClientEvent> getClientEventListeners(String eventName) {
		return clientEventHolder.getClientEventListeners(eventName);
	}

	public void clearClientEventListeners(String eventName) {
		clientEventHolder.clearClientEventListeners(eventName);
	}

	public Map<String, List<ClientEvent>> getAllClientEventListeners() {
		return clientEventHolder.getAllClientEventListeners();
	}

	/**
	 * 设置组件的id。
	 */
	public void setId(String id) {
		if (view != null) {
			throw new IllegalStateException(
					"Can not change the id property after the component attach to a view.");
		}
		this.id = id;
	}

	/**
	 * 返回组件的id。
	 */
	@XmlProperty(ignored = true, attributeOnly = true)
	@IdeProperty(highlight = 1)
	public String getId() {
		return id;
	}

	/**
	 * 返回控件的父控件，即控件所属的容器。
	 */
	@XmlProperty(unsupported = true)
	@IdeProperty(visible = false)
	public ViewElement getParent() {
		return parent;
	}

	/**
	 * 设置控件的父控件，即控件所属的容器。
	 */
	public void setParent(ViewElement parent) {
		ViewElementUtils.clearParentViewElement(this, this.parent);

		this.parent = parent;

		View view = ViewElementUtils.getParentView(parent);
		this.view = view;
		if (view != null) {
			ViewElementUtils.setParentViewElement(this, parent);
		}
	}

	@XmlProperty(unsupported = true)
	public View getView() {
		return view;
	}

	public ViewConfig getViewConfig() {
		return (view != null) ? view.getViewConfig() : null;
	}

	public void registerInnerElement(ViewElement element) {
		if (innerElements == null) {
			innerElements = new HashSet<ViewElement>();
		}
		innerElements.add(element);
	}

	public void unregisterInnerElement(ViewElement element) {
		if (innerElements != null) {
			innerElements.remove(element);
		}
	}

	public Collection<ViewElement> getInnerElements() {
		return innerElements;
	}

	@ClientProperty(ignored = true)
	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	/**
	 * 返回用于保存自定义信息的对象。
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * 设置用于保存自定义信息的对象。
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	@XmlProperty
	@ClientProperty
	@IdeProperty(editor = "any")
	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}

	@XmlProperty(composite = true)
	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}
}

class ComponentClientEventHolder extends ClientEventHolder {
	private Component component;

	@SuppressWarnings("unchecked")
	public ComponentClientEventHolder(Component component) {
		super((Class<? extends ClientEventSupported>) ProxyBeanUtils
				.getProxyTargetType(component));
		this.component = component;
	}

	@Override
	protected void checkEventAvailable(String eventName) {
		try {
			super.checkEventAvailable(eventName);
		} catch (IllegalArgumentException e) {
			AssembledComponentTypeRegisterInfo componentTypeRegisterInfo = (AssembledComponentTypeRegisterInfo) BeanExtender
					.getExProperty(component, "$assembledComponentInfo");
			if (componentTypeRegisterInfo == null) {
				throw e;
			} else {
				boolean valid = false;
				Map<String, VirtualEventDescriptor> virtualEvents = componentTypeRegisterInfo
						.getVirtualEvents();
				if (virtualEvents != null) {
					valid = virtualEvents.containsKey(eventName);
				}
				if (!valid) {
					throw new IllegalArgumentException(
							"Unrecognized client event ["
									+ componentTypeRegisterInfo.getName() + ","
									+ eventName + "].");
				}
			}
		}
	}
}
