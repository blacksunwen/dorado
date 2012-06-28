package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.Direction;
import com.bstek.dorado.view.widget.InnerElementReference;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-7
 */
@Widget(name = "SplitPanel", category = "General",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.SplitPanel",
		shortTypeName = "SplitPanel")
public class SplitPanel extends Control {
	private Direction direction = Direction.left;
	private InnerElementReference<Control> sideControlRef = new InnerElementReference<Control>(
			this);
	private InnerElementReference<Control> mainControlRef = new InnerElementReference<Control>(
			this);
	private String position = "100";
	private int minPosition;
	private int maxPosition;
	private boolean resizeable = true;
	private boolean collapsed;
	private boolean collapsable = true;
	private boolean previewable;

	@ClientProperty(escapeValue = "left")
	@IdeProperty(highlight = 1)
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "SideControl"))
	@ClientProperty
	public Control getSideControl() {
		return sideControlRef.get();
	}

	public void setSideControl(Control sideControl) {
		sideControlRef.set(sideControl);
	}

	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "MainControl"))
	@ClientProperty
	public Control getMainControl() {
		return mainControlRef.get();
	}

	public void setMainControl(Control mainControl) {
		mainControlRef.set(mainControl);
	}

	@ClientProperty(escapeValue = "100")
	@IdeProperty(highlight = 1)
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getMinPosition() {
		return minPosition;
	}

	public void setMinPosition(int minPosition) {
		this.minPosition = minPosition;
	}

	public int getMaxPosition() {
		return maxPosition;
	}

	public void setMaxPosition(int maxPosition) {
		this.maxPosition = maxPosition;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isResizeable() {
		return resizeable;
	}

	public void setResizeable(boolean resizeable) {
		this.resizeable = resizeable;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isCollapsable() {
		return collapsable;
	}

	public void setCollapsable(boolean collapsable) {
		this.collapsable = collapsable;
	}

	public boolean isPreviewable() {
		return previewable;
	}

	public void setPreviewable(boolean previewable) {
		this.previewable = previewable;
	}
}
