package com.bstek.dorado.desktop;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementReference;
import com.bstek.dorado.view.widget.base.SimpleButton;


@Widget(name = "Taskbar", category = "Desktop", dependsPackage = "desktop")
@ViewObject(prototype = "dorado.widget.desktop.Taskbar", shortTypeName = "desktop.Taskbar")
@XmlNode(nodeName = "Taskbar")
@ClientEvents({ @ClientEvent(name = "onTaskButtonContextMenu")})
public class Taskbar extends Control {
	private List<TaskButton> taskButtons;
	private List<QuickButton> quickButtons;
	private List<SimpleButton> trayButtons;
	private boolean showTimeLabel = true;
	
	private InnerElementReference<SimpleButton> startButtonRef = new InnerElementReference<SimpleButton>(
			this);
	
	/**
	 * @return the taskButtons
	 */
	@ViewAttribute
	@XmlSubNode(path = "TaskButtons/*")
	public List<TaskButton> getTaskButtons() {
		return taskButtons;
	}
	/**
	 * @param taskButtons the taskButtons to set
	 */
	public void setTaskButtons(List<TaskButton> taskButtons) {
		this.taskButtons = taskButtons;
	}
	/**
	 * @return the quickButtons
	 */
	@ViewAttribute
	@XmlSubNode(path = "QuickButtons/*")
	public List<QuickButton> getQuickButtons() {
		return quickButtons;
	}
	/**
	 * @param quickButtons the quickButtons to set
	 */
	public void setQuickButtons(List<QuickButton> quickButtons) {
		this.quickButtons = quickButtons;
	}
	/**
	 * @return the trayButtons
	 */
	@ViewAttribute
	@XmlSubNode(path = "TrayButtons/*")
	public List<SimpleButton> getTrayButtons() {
		return trayButtons;
	}
	/**
	 * @param trayButtons the trayButtons to set
	 */
	public void setTrayButtons(List<SimpleButton> trayButtons) {
		this.trayButtons = trayButtons;
	}
	
	/**
	 * @return the showTimeLabel
	 */
	@ViewAttribute(defaultValue = "true")
	public boolean isShowTimeLabel() {
		return showTimeLabel;
	}
	/**
	 * @param showTimeLabel the showTimeLabel to set
	 */
	public void setShowTimeLabel(boolean showTimeLabel) {
		this.showTimeLabel = showTimeLabel;
	}
	
	/**
	 * @return the startButton
	 */
	@ViewAttribute
	@XmlSubNode( path = "StartButton/*" )
	public SimpleButton getStartButton() {
		return startButtonRef.get();
	}
	
	/**
	 * @param startButton the startButton to set
	 */
	public void setStartButton(SimpleButton startButton) {
		startButtonRef.set(startButton);
	}
	
	
}
