package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.Direction;
import com.bstek.dorado.view.widget.InnerElementReference;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-7
 */
@Widget(name = "SplitPanel", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.SplitPanel", shortTypeName = "SplitPanel")
@XmlNode(nodeName = "SplitPanel")
public class SplitPanel extends Control {
	private Direction direction = Direction.left;
	private InnerElementReference<Control> sideControlRef = new InnerElementReference<Control>(
			this);
	private InnerElementReference<Control> mainControlRef = new InnerElementReference<Control>(
			this);
	private int position = 100;
	private int minPosition;
	private int maxPosition;
	private boolean resizeable = true;
	private boolean collapsed;
	private boolean collapsable = true;
	private boolean showCollapseBar;
	private boolean previewable;

	@ViewAttribute(defaultValue = "left")
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@ViewAttribute
	@XmlSubNode(path = "SideControl/*", fixed = true)
	public Control getSideControl() {
		return sideControlRef.get();
	}

	public void setSideControl(Control sideControl) {
		sideControlRef.set(sideControl);
	}

	@ViewAttribute
	@XmlSubNode(path = "MainControl/*", fixed = true)
	public Control getMainControl() {
		return mainControlRef.get();
	}

	public void setMainControl(Control mainControl) {
		mainControlRef.set(mainControl);
	}

	@ViewAttribute(defaultValue = "100")
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
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

	@ViewAttribute(defaultValue = "true")
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

	@ViewAttribute(defaultValue = "true")
	public boolean isCollapsable() {
		return collapsable;
	}

	public void setCollapsable(boolean collapsable) {
		this.collapsable = collapsable;
	}

	public boolean isShowCollapseBar() {
		return showCollapseBar;
	}

	public void setShowCollapseBar(boolean showCollapseBar) {
		this.showCollapseBar = showCollapseBar;
	}

	public boolean isPreviewable() {
		return previewable;
	}

	public void setPreviewable(boolean previewable) {
		this.previewable = previewable;
	}
}
