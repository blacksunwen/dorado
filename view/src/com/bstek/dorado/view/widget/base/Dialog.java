package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.FloatControlShadowMode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-6-2
 */
@Widget(name = "Dialog", category = "Floatable", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.Dialog", shortTypeName = "Dialog")
@XmlNode(nodeName = "Dialog")
@ClientEvents({ @ClientEvent(name = "beforeMinimize"),
		@ClientEvent(name = "onMinimize")})
public class Dialog extends FloatPanel {
	private int minWidth;
	private int minHeight;
	private boolean draggable = true;
	private boolean dragOutside;
	private boolean resizeable = true;
	
	private boolean minimizeable;
	private boolean minimized;
	private DialogStatus status = DialogStatus.normal;

	public Dialog() {
		setShadowMode(FloatControlShadowMode.frame);
	}

	@Override
	@ViewAttribute(defaultValue = "frame")
	public FloatControlShadowMode getShadowMode() {
		return super.getShadowMode();
	}

	public int getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}

	@Override
	@ViewAttribute(defaultValue = "true")
	public boolean isDraggable() {
		return draggable;
	}

	@Override
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isResizeable() {
		return resizeable;
	}

	public void setResizeable(boolean resizeable) {
		this.resizeable = resizeable;
	}

	public boolean isMinimizeable() {
		return minimizeable;
	}

	public void setMinimizeable(boolean minimizeable) {
		this.minimizeable = minimizeable;
	}

	public boolean isMinimized() {
		return minimized;
	}

	public void setMinimized(boolean minimized) {
		this.minimized = minimized;
	}

	@ViewAttribute(defaultValue = "normal")
	public DialogStatus getStatus() {
		return status;
	}

	public void setStatus(DialogStatus status) {
		this.status = status;
	}

	/**
	 * @return the dragOutside
	 */
	public boolean isDragOutside() {
		return dragOutside;
	}

	/**
	 * @param dragOutside the dragOutside to set
	 */
	public void setDragOutside(boolean dragOutside) {
		this.dragOutside = dragOutside;
	}
	
	
}
