package com.bstek.dorado.view.widget.base.menu;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.common.Ignorable;
import com.bstek.dorado.common.TagSupport;
import com.bstek.dorado.common.event.ClientEventSupportedObject;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.ViewElementUtils;
import com.bstek.dorado.view.widget.RenderableElement;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */
@XmlNode(implTypes = "com.bstek.dorado.view.widget.base.menu.*")
public abstract class BaseMenuItem extends ClientEventSupportedObject implements
		RenderableElement, Ignorable, TagSupport, ViewElement {
	private Collection<ViewElement> innerElements;

	private ViewElement parent;
	private String name;
	private String width;
	private String height;
	private String className;
	private String exClassName;
	private Map<String, Object> style;
	private boolean visible = true;
	private String tip;
	private boolean ignored;
	private String tags;

	public ViewElement getParent() {
		return parent;
	}

	public void setParent(ViewElement parent) {
		ViewElementUtils.clearParentViewElement(this, this.parent);

		this.parent = parent;

		View view = ViewElementUtils.getParentView(parent);
		if (view != null) {
			ViewElementUtils.setParentViewElement(this, parent);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getExClassName() {
		return exClassName;
	}

	public void setExClassName(String exClassName) {
		this.exClassName = exClassName;
	}

	@XmlProperty(composite = true)
	public Map<String, Object> getStyle() {
		return style;
	}

	public void setStyle(Map<String, Object> style) {
		this.style = style;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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
}
